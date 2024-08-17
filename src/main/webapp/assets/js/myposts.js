document.addEventListener('DOMContentLoaded', async function () {
  let posts = await makeHttpRequest('/posts/myposts')
  posts = posts
    .sort((a, b) => b.createdAt - a.createdAt)
    .map(post => {
      return {
        ...post,
        imageUrls: post.imageUrls.length > 0 ? post.imageUrls : ['/assets/css/images/post-default.png']
      }
    })

  var postContainer = document.getElementById('post-container')
  var postModal = document.getElementById('postModal')
  var modalTitle = document.getElementById('modalTitle')
  var modalContent = document.getElementById('modalContent')
  var carouselImages = document.getElementById('carousel-images')
  var closeModal = document.querySelector('.close')
  var prevButton = document.querySelector('.prev')
  var nextButton = document.querySelector('.next')
  var currentImageIndex = 0
  var imageElements = []

  var createPostModal = document.getElementById('createPostModal')
  var closeCreateModal = document.querySelector('.close-create')
  var createPostTitle = document.getElementById('createPostTitle')
  var createPostType = document.getElementById('createPostType')
  var createPostContent = document.getElementById('createPostContent')
  var createPostLocation = document.getElementById('createPostLocation')
  var createPostImageUpload = document.getElementById('createPostImageUpload')
  var createPostButton = document.getElementById('createPostButton')
  var createPostBtn = document.getElementById('create-post-btn')

  var editPostModal = document.getElementById('editPostModal')
  var closeEditModal = document.querySelector('.close-edit')
  var editPostForm = document.getElementById('editPostForm')
  var editTitle = document.getElementById('editTitle')
  var editContent = document.getElementById('editContent')
  var editLocation = document.getElementById('editLocation')
  var editImageUrls = document.getElementById('editImageUrls')
  var uploadNewImages = document.getElementById('uploadNewImages')
  var editPostIndex = document.getElementById('editPostIndex')

  var goToHomeBtn = document.getElementById('go-to-home-btn')

  function renderPosts() {
    postContainer.innerHTML = ''

    if (posts.length === 0) {
      console.log('No posts found.')
      postContainer.innerHTML = '<p>No posts available. Please create a new post.</p>'
      return
    }

    posts.forEach(function (post, index) {
      const colorClass = post.type == 1 ? 'green' : post.type == 2 ? 'yellow' : 'blue'

      var postElement = document.createElement('div')
      postElement.classList.add('box', colorClass)
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
                        <div class="more-options-container">
                            <button class="more-options"><i class="fas fa-ellipsis-h"></i></button>
                            <div class="dropdown-menu">
                                <button class="button edit-button" data-index="${index}"><i class="fas fa-edit"></i> Edit</button>
                                <button class="button delete-button" data-index="${index}"><i class="fas fa-trash-alt"></i> Delete</button>
                            </div>
                        </div>
                    </div>
                `

      postContainer.appendChild(postElement)

      // Event listener for the "more options" button
      postElement.querySelector('.more-options').addEventListener('click', function (event) {
        event.stopPropagation()
        var dropdownMenu = this.nextElementSibling
        if (dropdownMenu.style.display === 'none' || !dropdownMenu.style.display) {
          dropdownMenu.style.display = 'block'
          setTimeout(() => {
            dropdownMenu.style.maxHeight = dropdownMenu.scrollHeight + 'px'
            dropdownMenu.style.opacity = '1'
            dropdownMenu.style.padding = '10px'
          }, 10) // Small delay to trigger transition
        } else {
          dropdownMenu.style.maxHeight = '0'
          dropdownMenu.style.opacity = '0'
          dropdownMenu.style.padding = '0'
          setTimeout(() => {
            dropdownMenu.style.display = 'none'
          }, 300) // match the transition duration
        }
      })

      // Event listener for the edit button
      postElement.querySelector('.edit-button').addEventListener('click', function (event) {
        event.stopPropagation()
        var index = parseInt(this.dataset.index, 10)
        openEditModal(index)
      })

      // Event listener for the delete button
      postElement.querySelector('.delete-button').addEventListener('click', function (event) {
        event.stopPropagation()
        var index = parseInt(this.dataset.index, 10)
        deletePost(index)
      })

      postElement.addEventListener('click', function (event) {
        event.preventDefault()
        openModal(post)
      })

      // Close dropdown when clicking outside
      window.addEventListener('click', function (event) {
        if (!postElement.contains(event.target)) {
          var dropdownMenu = postElement.querySelector('.dropdown-menu')
          dropdownMenu.style.maxHeight = '0'
          dropdownMenu.style.opacity = '0'
          dropdownMenu.style.padding = '0'
          setTimeout(() => {
            dropdownMenu.style.display = 'none'
          }, 300)
        }
      })
    })
  }

  renderPosts() // Initial render

  function openModal(post) {
    modalTitle.innerHTML = `<span class="username">@${post.createdBy.toLowerCase()}</span> ${post.title}`
    modalContent.innerHTML = `<p>${post.description}</p>${
      post.type === 3
        ? `<p>
          <strong>Location:</strong> ${post.location === 'undefined' ? 'No location information' : post.location}
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

  function openEditModal(index) {
    var post = posts[index]
    editTitle.value = post.title
    editDescription.value = post.description
    editLocation.value = post.location
    editImageUrls.value = post.imageUrls.join(', ')
    editPostIndex.value = index

    editPostModal.style.display = 'block'
    setTimeout(() => {
      editPostModal.style.opacity = '1'
    }, 50)
  }

  function closeEditModalHandler() {
    editPostModal.style.opacity = '0'
    setTimeout(() => {
      editPostModal.style.display = 'none'
    }, 500)
  }

  closeEditModal.addEventListener('click', closeEditModalHandler)

  window.addEventListener('click', function (event) {
    if (event.target === editPostModal) {
      closeEditModalHandler()
    }
  })

  editPostForm.addEventListener('submit', function (event) {
    event.preventDefault()

    var index = parseInt(editPostIndex.value, 10)
    var updatedPost = {
      title: editTitle.value,
      description: editDescription.value,
      //   location: editLocation.value,
      // Ensure existing images are included
      //   imageUrls: posts[index].imageUrls.slice(),

      type: posts[index].type
    }
    makeHttpRequest('/posts/' + posts[index].id, 'PUT', updatedPost).then(response => {
      if (response.status === 201) {
        alert('Post updated successfully.')
        window.location.reload()
      } else {
        alert('Failed to update post. Please try again later.')
      }
    })

    // var newImages = Array.from(uploadNewImages.files)

    // if (newImages.length > 0) {
    //   var imageFilesProcessed = 0
    //   var newImageUrls = []

    //   newImages.forEach(file => {
    //     var reader = new FileReader()
    //     reader.onload = function (e) {
    //       newImageUrls.push(e.target.result)
    //       imageFilesProcessed++
    //       if (imageFilesProcessed === newImages.length) {
    //         // Append new images to existing ones
    //         updatedPost.imageUrls = updatedPost.imageUrls.concat(newImageUrls)
    //         posts[index] = updatedPost
    //         localStorage.setItem('posts', JSON.stringify(posts))
    //         renderPosts() // Refresh posts
    //         closeEditModalHandler()
    //       }
    //     }
    //     reader.readAsDataURL(file)
    //   })
    // } else {
    //   posts[index] = updatedPost
    //   localStorage.setItem('posts', JSON.stringify(posts))
    //   renderPosts() // Refresh posts
    //   closeEditModalHandler()
    // }
  })

  function deletePost(index) {
    if (confirm('Are you sure you want to delete this post?')) {
      makeHttpRequest('/posts/' + posts[index].id, 'DELETE')
      posts.splice(index, 1)
      renderPosts() // Refresh posts
    }
  }

  if (createPostBtn) {
    createPostBtn.addEventListener('click', function () {
      createPostModal.style.display = 'block'
      setTimeout(() => {
        createPostModal.style.opacity = '1'
      }, 50)
    })
  }

  if (closeCreateModal) {
    closeCreateModal.addEventListener('click', function () {
      createPostModal.style.opacity = '0'
      setTimeout(() => {
        createPostModal.style.display = 'none'
      }, 500)
    })
  }

  window.addEventListener('click', function (event) {
    if (event.target === createPostModal) {
      createPostModal.style.opacity = '0'
      setTimeout(() => {
        createPostModal.style.display = 'none'
      }, 500)
    }
  })

  if (createPostButton) {
    createPostButton.addEventListener('click', function () {
      var title = createPostTitle.value
      var type = createPostType.value
      var description = createPostDescription.value
      // var location = createPostLocation.value
      // var imageFiles = Array.from(createPostImageUpload.files)
      // var imageUrls = []

      if (title && type && description) {
        var imageFilesProcessed = 0

        // imageFiles.forEach(file => {
        //   var reader = new FileReader()
        //   reader.onload = function (e) {
        //     imageUrls.push(e.target.result)
        //     imageFilesProcessed++
        //     if (imageFilesProcessed === imageFiles.length) {

        //       posts.push(newPost)
        //       localStorage.setItem('posts', JSON.stringify(posts))
        //       renderPosts() // Refresh posts
        //       createPostModal.style.opacity = '0'
        //       setTimeout(() => {
        //         createPostModal.style.display = 'none'
        //       }, 500)
        //     }
        //   }
        //   reader.readAsDataURL(file)
        // })

        var body = {
          title: title,
          postType: type,
          description: description
        }

        makeHttpRequest('/posts', 'POST', body).then(response => {
          if (response.status === 201) {
            alert('Post created successfully.')
            window.location.reload()
          } else {
            alert('Failed to create post. Please try again later.')
          }
        })
      } else {
        alert('Please fill in all fields and upload at least one image.')
      }
    })
  }

  document.getElementById('go-to-liked-btn').addEventListener('click', function () {
    window.location.href = '/notebridge/myliked'
  })

  if (goToHomeBtn) {
    goToHomeBtn.addEventListener('click', function () {
      window.location.href = '/notebridge/'
    })
  }

  // Generate a unique ID for each post
  function generateUniquePostId() {
    const postId = 'post-' + Math.random().toString(36).substr(2, 9) // Unique ID based on random string
    return postId
  }
})
