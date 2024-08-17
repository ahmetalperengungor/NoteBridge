;(function ($) {
  // DOM elements
  const $window = $(window),
    $body = $('body'),
    $main = $('#main'),
    $createPostForm = $('.create-post-form'),
    $postButton = $('#post-button'),
    $createPostFormButton = $('#create-post-form-button'),
    $myProfile = $('#profile-page'),
    $modal = $('#postModal'),
    $modalTitle = $('#modalTitle'),
    $modalSubmitCommentButton = $('.modal-submit-comment-button'),
    $carouselImages = $('#carousel-images'),
    $modalContent = $('#modalContent'),
    $modalLikeButton = $('.like-button'),
    $closeModal = $('.close'),
    $prev = $('.prev'),
    $next = $('.next')
  const commentsContainer = document.getElementById('comments-container')

  // Add CSS styles
  const styles = `
        .username {
            font-size: 0.9em; /* Smaller font size */
            text-transform: lowercase; /* Ensure lowercase */
            margin-right: 10px; /* Space between username and title */
        }
    `
  $('<style>').text(styles).appendTo('head')

  // Hide the form initially
  $createPostForm.hide()

  // Toggle form visibility when the button is clicked
  $createPostFormButton.on('click', function () {
    $createPostForm.toggle()
  })

  // Convert image file to base64
  function getBase64(file, callback) {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => callback(reader.result)
    reader.onerror = error => console.error('Error: ', error)
  }

  // Handle post creation
  $postButton.on('click', function () {
    const type = $('.create-post-form-type').val()
    const colorClass = type == 1 ? 'green' : type == 2 ? 'yellow' : 'blue'
    const title = $('.create-post-form-title').val()
    const description = $('.create-post-form-content').val()
    const location = $('.create-post-form-location').val()
    const files = document.getElementById('image-upload').files
    console.log('Files: ', files)
    if (files.length === 0) {
      alert('Please select at least one image to upload.')
      return
    }

    savePost({ title, sponsorId: null, eventId: null, description, postType: type, files: files })
  })

  // Generate a unique ID for each post
  function generateUniquePostId() {
    const postId = 'post-' + new Date().getTime() // Unique ID based on timestamp
    return postId
  }

  // Save post and update the UI
  async function savePost(post) {
    const files = post.files
    let body = {
      title: post.title,
      description: post.description,
      postType: post.postType,
      sponsorId: post.sponsorId,
      eventId: post.eventId
    }

    const response = await makeHttpRequest('/posts', 'POST', body)
    // get files from a file list
    for (let i = 0; i < files.length; i++) {
      const file = files[i]
      getBase64(file, async base64String => {
        const formData = new FormData()
        formData.append('file', file)
        makeXHRRequest('/posts/uploadPostPicture/' + response.data.id, formData)
      })
    }
    clearForm()
    const { data } = await makeHttpRequest('/posts')
    const posts = data.sort((a, b) => b.createdAt - a.createdAt)
    document.getElementById('main').innerHTML = ''
    posts.forEach(displayPost)
    $createPostForm.hide()
  }

  // Clear form fields
  function clearForm() {
    $('.create-post-form-title').val('')
    $('.create-post-form-content').val('')
    $('.create-post-form-location').val('')
    $('#image-upload').val('')
  }

  // Event delegation for dynamically added posts to show in a modal
  $main.on('click', '.thumb a.image', async function () {
    const title = $(this).data('title')
    const content = $(this).data('content')
    const location = $(this).data('location')
    const images = $(this).data('images')
    const username = $(this).data('username') // Get username from data attribute
    const id = $(this).data('post-id')
    const isLiked = $(this).data('is-liked')
    const comments = await makeHttpRequest('/comments/post/' + id)
    loadComments(comments)

    $modalTitle.html(`<p class="username">@${username.toLowerCase()}</p> ${title}`)
    $modalContent.html(
      `<p>${content}</p><p><strong>Location:</strong> ${
        location === 'undefined' ? 'No location information' : location
      }</p>`
    )
    // add post id to modalSubmitCommentButton
    $modalSubmitCommentButton.attr('data-post-id', id)
    $modalLikeButton.attr('data-post-id', id)
    if (isLiked) {
      // make the like button class active
      $modalLikeButton.addClass('active')
    } else {
      $modalLikeButton.removeClass('active')
    }

    // when click the modalTitle, go profile page
    $('.username').on('click', function () {
      window.location.href = '/notebridge/profile/' + username
    })

    $carouselImages.empty()

    // Add images to the carousel
    images.forEach((image, index) => {
      const imgElement = `<img src="${image}" class="${index === 0 ? 'active' : ''}" alt="Uploaded Image">`
      $carouselImages.append(imgElement)
    })

    // Show or hide navigation arrows
    if (images.length > 1) {
      $prev.show()
      $next.show()
    } else {
      $prev.hide()
      $next.hide()
    }

    // Show the modal with fade-in effect
    $modal.css('opacity', '0')
    $modal.show()
    $modal.animate({ opacity: 1 }, 500)
  })

  // Close modal when 'x' is clicked
  $closeModal.on('click', function () {
    $modal.animate({ opacity: 0 }, 500, function () {
      $modal.hide()
    })
  })

  // Close modal when clicked outside of content
  $window.on('click', function (event) {
    if (event.target === $modal[0]) {
      $modal.animate({ opacity: 0 }, 500, function () {
        $modal.hide()
      })
    }
  })

  // Carousel navigation
  $prev.on('click', function () {
    const $active = $carouselImages.find('img.active')
    const $prevImg = $active.prev('img')
    if ($prevImg.length) {
      $active.removeClass('active')
      $prevImg.addClass('active')
    } else {
      $active.removeClass('active')
      $carouselImages.find('img').last().addClass('active')
    }
  })

  $next.on('click', function () {
    const $active = $carouselImages.find('img.active')
    const $nextImg = $active.next('img')
    if ($nextImg.length) {
      $active.removeClass('active')
      $nextImg.addClass('active')
    } else {
      $active.removeClass('active')
      $carouselImages.find('img').first().addClass('active')
    }
  })

  // Show My Posts section
  $myProfile.on('click', function () {
    window.location.href = '/notebridge/myposts'
  })

  // Load posts from database
  $(document).ready(async function () {
    const { data } = await makeHttpRequest('/posts')
    const posts = data.sort((a, b) => b.createdAt - a.createdAt)
    posts.forEach(displayPost)
  })

  // Function to display a new post
  function displayPost(post) {
    let typeText = post.postType === 0 ? 'Question' : post.postType === 1 ? 'Discussion' : 'Event'
    switch (post.postType) {
      case 0:
        typeText = 'Question'
        break
      case 1:
        typeText = 'Discussion'
        break
      case 2:
        typeText = 'Event'
        break
      case 3:
        typeText = 'Sponsored'
        break
      default:
        typeText = 'General'
        break
    }
    const colorClass = post.postType == 0 ? 'green' : post.postType == 1 ? 'yellow' : 'blue'

    post.imageUrls = post.imageUrls.length ? post.imageUrls : ['/assets/css/images/post-default.png']

    const isSponsored = post.postType === 3

    const postContent =
      `
      <div class="post ${post.postType === 3 ? 'sponsored' : ''}" data-post-id="${post.id}">
          <article class="thumb ${colorClass}">
              <a href="javascript:void(0);" class="image" data-title="${post.title}" data-content="${
        post.description
      }" data-location="${post.location}" data-images='${JSON.stringify(post.imageUrls)}' data-username="${
        post.createdBy
      }" data-post-id="${post.id}" data-is-liked="${post.isLiked}">
                  <img src="${post.imageUrls[0]}" alt="Uploaded Image" class="post-container-small-image"/>
                  <div class="overlay">
                      <div class="post-type" ${isSponsored ? 'sponsored' : ''}>${typeText}</div>
                      <div class="post-title ${isSponsored ? 'sponsored' : ''}">${post.title}</div>
                  </div>
              </a>
              <p class="post-description2">${post.description}</p>
              ` +
      (post.postType === 3
        ? `<p>
          <strong>Location:</strong> ${post.location}
        </p>`
        : ``) +
      `<div class="modal-buttons">
                        <span class="like-button ${post.id}" data-post-id="${post.id}"><i class="fa fa-heart"></i> Like</span>
                        <button class="comment-button ${post.id}" data-post-id="${post.id}"><i class="fa fa-comment"></i> Comment</button>
                    </div>
          </article>
          <!-- Comment Section -->
          <div class="comment-section">
              <div class="comments-container" data-post-id="${post.id}"></div>
          </div>
      </div>
      `

    document.getElementById('main').insertAdjacentHTML('beforeend', postContent)
    // when click the modalTitle
    $('.modalTitle').on('click', function () {
      window.location.href = '/notebridge/profile/' + post.id
    })
  }

  // Function to add comment to the comments container
  function addComment({ username, comment }) {
    console.log('adding comments')
    const commentElement = document.createElement('div')
    commentElement.className = 'comment'
    commentElement.innerHTML = `<strong>${username}:</strong> ${comment}`
    commentsContainer.appendChild(commentElement)
    commentsContainer.classList.remove('hidden') // Ensure the comments container is visible
    console.log('Comment added to container')
  }

  // Load comments from local storage
  function loadComments(comments) {
    commentsContainer.innerHTML = '' // Clear existing comments
    comments.map(comment => addComment(comment))
  }
})(jQuery)
