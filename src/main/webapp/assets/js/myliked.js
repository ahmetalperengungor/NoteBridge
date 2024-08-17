document.addEventListener('DOMContentLoaded', async function () {
  var postContainer = document.getElementById('post-container')
  var userProfile = JSON.parse(localStorage.getItem('userProfile')) || {}
  var posts = await makeHttpRequest('/likes/' + userProfile.id, 'GET')
  var postModal = document.getElementById('postModal')
  var modalTitle = document.getElementById('modalTitle')
  var modalContent = document.getElementById('modalContent')
  var carouselImages = document.getElementById('carousel-images')
  var closeModal = document.querySelector('.close')
  var prevButton = document.querySelector('.prev')
  var nextButton = document.querySelector('.next')
  var currentImageIndex = 0
  var imageElements = []

  var goToHomeBtn = document.getElementById('go-to-home-btn')
  var goToMyPostsBtn = document.getElementById('go-to-my-posts-btn')

  function renderPosts() {
    postContainer.innerHTML = ''

    if (posts.length === 0) {
      console.log('No liked posts found.')
      postContainer.innerHTML = '<p>No liked posts available. Please like some posts to view them here.</p>'
      return
    }

    posts.forEach(function (post, index) {
      if (post.description && post.title) {
        console.log('Adding post:', post)
        const colorClass = post.postType == 1 ? 'green' : post.postType == 2 ? 'yellow' : 'blue'

        var postElement = document.createElement('div')
        postElement.classList.add('box', post.colorClass)
        postElement.dataset.index = index
        postElement.innerHTML = `
                    <div class="img-area">
                        <img src="${post.imageUrls[0]}" alt="Post Image">
                    </div>
                    <div class="content">
                        <h3>${post.title}</h3>
                        <p>${post.description}</p>
                        ${
                          post.type === 3
                            ? `<p>
                              <strong>Location:</strong> ${post.location}
                            </p>`
                            : ''
                        }
                    </div>
                `

        postContainer.appendChild(postElement)

        postElement.addEventListener('click', function (event) {
          event.preventDefault()
          openModal(post)
        })
      } else {
        console.log(`Invalid post data for post ${index + 1}:`, post)
      }
    })
  }

  renderPosts() // Initial render

  function openModal(post) {
    modalTitle.innerHTML = `<span class="username">@${post.createdBy.toLowerCase()}</span> ${post.title}`
    modalContent.innerHTML = `<p>${post.description}</p>${
      post.type === 1
        ? `<p>
          <strong>Location:</strong> ${post.location}
        </p>`
        : ''
    }`
    carouselImages.innerHTML = ''
    currentImageIndex = 0
    imageElements = []

    post.imageUrls.forEach((image, index) => {
      var imgElement = document.createElement('img')
      imgElement.src = image
      imgElement.className = index === 0 ? 'active' : ''
      imgElement.style.height = '100%'
      imgElement.style.width = '100%'
      imgElement.style.objectFit = 'contain'
      carouselImages.appendChild(imgElement)
      imageElements.push(imgElement)
    })

    postModal.style.display = 'block'
    setTimeout(() => {
      postModal.style.opacity = '1'
    }, 50)
    updateCarousel()
  }

  function closeModalHandler() {
    postModal.style.opacity = '0'
    setTimeout(() => {
      postModal.style.display = 'none'
    }, 500)
  }

  function updateCarousel() {
    imageElements.forEach((img, index) => {
      img.classList.toggle('active', index === currentImageIndex)
    })
    prevButton.style.display = imageElements.length > 1 ? 'block' : 'none'
    nextButton.style.display = imageElements.length > 1 ? 'block' : 'none'
  }

  closeModal.addEventListener('click', closeModalHandler)

  window.addEventListener('click', function (event) {
    if (event.target === postModal) {
      closeModalHandler()
    }
  })

  prevButton.addEventListener('click', function () {
    currentImageIndex = currentImageIndex > 0 ? currentImageIndex - 1 : imageElements.length - 1
    updateCarousel()
  })

  nextButton.addEventListener('click', function () {
    currentImageIndex = currentImageIndex < imageElements.length - 1 ? currentImageIndex + 1 : 0
    updateCarousel()
  })

  goToHomeBtn.addEventListener('click', function () {
    window.location.href = '/notebridge/'
  })

  goToMyPostsBtn.addEventListener('click', function () {
    window.location.href = '/notebridge/myposts'
  })
})
