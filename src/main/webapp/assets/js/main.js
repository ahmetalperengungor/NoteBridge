;(function ($) {
  document.addEventListener('DOMContentLoaded', function () {
    const commentButtons = document.querySelectorAll('.comment-button')
    const commentForm = document.getElementById('comment-form')
    const commentInput = document.getElementById('comment-input')
    const submitCommentButton = document.getElementById('submit-comment')
    const commentsContainer = document.getElementById('comments-container')
    const likeButtons = document.querySelectorAll('.like-button')

    // Retrieve the userProfile from local storage and parse it
    const userProfile = localStorage.getItem('userProfile')
    let username = 'Anonymous' // Default to 'Anonymous' if username is not found

    if (userProfile) {
      try {
        const userProfileObj = JSON.parse(userProfile)
        if (userProfileObj && userProfileObj.username) {
          username = userProfileObj.username
        }
      } catch (error) {
        console.error('Error parsing userProfile:', error)
      }
    }

    // Add event listener to each comment button
    commentButtons.forEach(button => {
      button.addEventListener('click', function () {
        commentForm.classList.toggle('hidden')
        commentsContainer.classList.toggle('hidden')
        console.log('Comment form toggled')
      })
    })

    // Add event listener to submit comment button
    submitCommentButton.addEventListener('click', function () {
      const commentText = commentInput.value.trim()
      if (commentText) {
        username = JSON.parse(localStorage.getItem('userProfile')).email
        const commentData = {
          username: username,
          text: commentText
        }
        const body = {
          userId: JSON.parse(localStorage.getItem('userProfile')).id,
          postId: document.querySelector('.modal-submit-comment-button').getAttribute('data-post-id'), // Get post ID from the modal submit button
          comment: commentText
        }
        makeHttpRequest('/comments', 'POST', body)
        addComment(commentData)
        commentInput.value = '' // Clear input field
        console.log('Comment submitted')
      } else {
        console.warn('Empty comment text')
      }
    })

    // Add event listener to each like button
    likeButtons.forEach(button => {
      button.addEventListener('click', async function () {
        const postId = button.getAttribute('data-post-id')
        button.classList.toggle('active')

        if (postId) {
          let likedPosts = await makeHttpRequest('/likes/' + JSON.parse(localStorage.getItem('userProfile')).id)

          likedPosts = likedPosts.map(post => post.id)

          if (button.classList.contains('active')) {
            // Add post ID to likedPosts
            if (!likedPosts.includes(postId)) {
              makeHttpRequest('/likes', 'POST', {
                userId: JSON.parse(localStorage.getItem('userProfile')).id,
                postId: postId
              })
            }
          } else {
            // Remove post ID from likedPosts
            makeHttpRequest('/' + postId + '/' + JSON.parse(localStorage.getItem('userProfile')).id, 'DELETE')
          }
          console.log(`Post ${postId} liked status: ${button.classList.contains('active')}`)
        } else {
          console.warn('Post ID not found for like button')
        }
      })
    })
  })

  var $window = $(window),
    $body = $('body'),
    $wrapper = $('#wrapper')

  // Breakpoints.
  breakpoints({
    xlarge: ['1281px', '1680px'],
    large: ['981px', '1280px'],
    medium: ['737px', '980px'],
    small: ['481px', '736px'],
    xsmall: [null, '480px']
  })

  // Hack: Enable IE workarounds.
  if (browser.name == 'ie') {
    $body.addClass('ie')
  }

  // Touch?
  if (browser.mobile) {
    $body.addClass('touch')
  }

  // Transitions supported?
  if (browser.canUse('transition')) {
    // Play initial animations on page load.
    $window.on('load', function () {
      window.setTimeout(function () {
        $body.removeClass('is-preload')
      }, 100)
    })

    // Prevent transitions/animations on resize.
    var resizeTimeout

    $window.on('resize', function () {
      window.clearTimeout(resizeTimeout)
      $body.addClass('is-resizing')

      resizeTimeout = window.setTimeout(function () {
        $body.removeClass('is-resizing')
      }, 100)
    })
  }

  // Scroll back to top.
  $window.scrollTop(0)

  // Panels.
  var $panels = $('.panel')

  $panels.each(function () {
    var $this = $(this),
      $toggles = $('[href="#' + $this.attr('id') + '"]'),
      $closer = $('<div class="closer" />').appendTo($this)

    // Closer.
    $closer.on('click', function (event) {
      $this.trigger('---hide')
    })

    // Events.
    $this
      .on('click', function (event) {
        event.stopPropagation()
      })
      .on('---toggle', function () {
        if ($this.hasClass('active')) $this.triggerHandler('---hide')
        else $this.triggerHandler('---show')
      })
      .on('---show', function () {
        // Hide other content.
        if ($body.hasClass('content-active')) $panels.trigger('---hide')

        // Activate content, toggles.
        $this.addClass('active')
        $toggles.addClass('active')

        // Activate body.
        $body.addClass('content-active')
      })
      .on('---hide', function () {
        // Deactivate content, toggles.
        $this.removeClass('active')
        $toggles.removeClass('active')

        // Deactivate body.
        $body.removeClass('content-active')
      })

    // Toggles.
    $toggles
      .removeAttr('href')
      .css('cursor', 'pointer')
      .on('click', function (event) {
        event.preventDefault()
        event.stopPropagation()
        $this.trigger('---toggle')
      })
  })

  // Global events.
  $body.on('click', function (event) {
    if ($body.hasClass('content-active')) {
      event.preventDefault()
      event.stopPropagation()
      $panels.trigger('---hide')
    }
  })

  $window.on('keyup', function (event) {
    if (event.keyCode === 27 && $body.hasClass('content-active')) {
      event.preventDefault()
      event.stopPropagation()
      $panels.trigger('---hide')
    }
  })

  // Header.
  var $header = $('#header')

  // Links.
  $header.find('a').each(function () {
    var $this = $(this),
      href = $this.attr('href')

    // Internal link? Skip.
    if (!href || href.charAt(0) === '#') return

    // Redirect on click.
    $this
      .removeAttr('href')
      .css('cursor', 'pointer')
      .on('click', function (event) {
        event.preventDefault()
        event.stopPropagation()
        window.location.href = href
      })
  })

  // Footer.
  var $footer = $('#footer')

  // This moves the copyright line to the end of the last sibling of its current parent
  // when the "medium" breakpoint activates, and moves it back when it deactivates.
  $footer.find('.copyright').each(function () {
    var $this = $(this),
      $parent = $this.parent(),
      $lastParent = $parent.parent().children().last()

    breakpoints.on('<=medium', function () {
      $this.appendTo($lastParent)
    })

    breakpoints.on('>medium', function () {
      $this.appendTo($parent)
    })
  })

  // Main.
  var $main = $('#main')

  // Thumbs.
  $main.children('.thumb').each(function () {
    var $this = $(this),
      $image = $this.find('.image'),
      $image_img = $image.children('img')

    // No image? Bail.
    if ($image.length === 0) return

    // This sets the background of the "image" <span> to the image pointed to by its child
    // <img> (which is then hidden). Gives us more flexibility.
    $image.css('background-image', 'url(' + $image_img.attr('src') + ')')

    // Set background position.
    if ($image_img.data('position')) $image.css('background-position', $image_img.data('position'))

    // Hide original img.
    $image_img.hide()
  })

  // Poptrox.
  $main.poptrox({
    baseZIndex: 20000,
    caption: function ($a) {
      var s = ''
      $a.nextAll().each(function () {
        s += this.outerHTML
      })
      return s
    },
    fadeSpeed: 300,
    onPopupClose: function () {
      $body.removeClass('modal-active')
    },
    onPopupOpen: function () {
      $body.addClass('modal-active')
    },
    overlayOpacity: 0,
    popupCloserText: '',
    popupHeight: 150,
    popupLoaderText: '',
    popupSpeed: 300,
    popupWidth: 150,
    selector: '.thumb > a.image',
    usePopupCaption: true,
    usePopupCloser: true,
    usePopupDefaultStyling: false,
    usePopupForceClose: true,
    usePopupLoader: true,
    usePopupNav: true,
    windowMargin: 50
  })

  // Hack: Set margins to 0 when 'xsmall' activates.
  breakpoints.on('<=xsmall', function () {
    $main[0]._poptrox.windowMargin = 0
  })

  breakpoints.on('>xsmall', function () {
    $main[0]._poptrox.windowMargin = 50
  })

  document.addEventListener('DOMContentLoaded', function () {
    const mainContainer = document.getElementById('main')

    mainContainer.addEventListener('click', function (event) {
      if (event.target.closest('.comment-button')) {
        const commentButton = event.target.closest('.comment-button')
        const postId = commentButton.getAttribute('data-post-id')
        console.log(`Comment button clicked for post ID: ${postId}`)
      }
    })
  })

  // filter posts according to search-input input
  document.getElementById('search-input').addEventListener('input', function () {
    const searchInput = this.value.toLowerCase()
    const posts = document.querySelectorAll('.post')
    if (!searchInput) {
      posts.forEach(post => {
        post.style.display = 'flex'
      })
      return
    }
    console.log('filtered post', posts)

    posts.forEach(post => {
      const title = post.querySelector('.post-title').textContent.toLowerCase()
      const description = post.querySelector('.post-description2').textContent.toLowerCase()
      if (title.includes(searchInput) || description.includes(searchInput)) {
        post.style.display = 'flex'
      } else {
        post.style.display = 'none'
      }
    })
  })
})(jQuery)
