document.addEventListener('DOMContentLoaded', async function () {
  const editProfileButton = document.getElementById('editProfileButton')
  const editProfileForm = document.getElementById('editProfileForm')
  const profileForm = document.getElementById('profileForm')
  const displayName = document.getElementById('displayName')
  const displayEmail = document.getElementById('displayEmail')
  const displayDescription = document.getElementById('displayDescription')
  const profilePic = document.getElementById('profilePic')
  const profilePicInput = document.getElementById('profilePicInput')
  const twitterLink = document.getElementById('twitterLink')
  const instagramLink = document.getElementById('instagramLink')
  const spotifyLink = document.getElementById('spotifyLink')
  const musicalInterestSelect = document.getElementById('musicalInterestSelect')
  const addInterestButton = document.getElementById('addInterestButton')
  const addCustomInterestButton = document.getElementById('addCustomInterestButton')
  const customInterestInput = document.getElementById('customInterestInput')
  const displayInterests = document.getElementById('displayInterests')
  const newActivity = document.getElementById('newActivity')
  const recentActivity = document.getElementById('recentActivity')
  const friendSearch = document.getElementById('friendSearch')
  const searchFriendsButton = document.getElementById('searchFriendsButton')
  const friendsSearchResults = document.getElementById('friendsSearchResults')
  const myFriendsList = document.getElementById('myFriendsList')
  const likedPostsGrid = document.getElementById('likedPostsGrid')

  let userProfile = {}
  let isMe = false

  // take the latest / for the username
  const username = window.location.pathname.split('/').pop()

  // const username = window.location.pathname.split('/').pop()

  if (username === 'profile') {
    userProfile = JSON.parse(localStorage.getItem('userProfile'))
    isMe = true
  } else {
    userProfile = await makeHttpRequest('/users/getUserByMail/' + username, 'GET')
  }

  // Retrieve user profile from localStorage

  // Update profile information if userProfile exists
  if (userProfile) {
    updateProfileFields(userProfile) // Function to update fields
  }

  function updateProfileFields(userProfile) {
    displayName.textContent = userProfile.firstName + ' ' + userProfile.lastName
    displayEmail.textContent = userProfile.email
    displayDescription.textContent = userProfile.description
    if (userProfile.imageUrl) {
      profilePic.src = userProfile.imageUrl
    }

    // Set form fields to current values
    profileForm.name.value = userProfile.firstName + ' ' + userProfile.lastName
    profileForm.email.value = userProfile.email
    profileForm.description.value = userProfile.description

    // Update phone number (assuming userProfile.phone is in the format "+xx yyyyyyy")
    const phoneCountryCode = userProfile.phoneNumber && userProfile.phoneNumber.substring(0, 3)
    const phoneNumber = userProfile.phoneNumber && userProfile.phoneNumber.substring(3)
    profileForm.phoneCountryCode.value = phoneCountryCode
    profileForm.phone.value = phoneNumber

    fetchUserInterests()
    updateLikedPosts()
  }

  async function updateLikedPosts() {
    const likedPosts = await makeHttpRequest('/likes/' + userProfile.id, 'GET')

    likedPostsGrid.innerHTML = ''
    likedPosts.forEach(post => {
      const postElement = document.createElement('div')
      postElement.className = 'liked-post'
      const img = document.createElement('img')
      img.src = post.imageUrls[0]
      img.alt = post.description
      postElement.appendChild(img)
      likedPostsGrid.appendChild(postElement)
    })
  }

  editProfileButton.addEventListener('click', function () {
    editProfileForm.classList.toggle('hidden')
    if (!editProfileForm.classList.contains('hidden')) {
      // Update fields with current profile data when editing
      if (userProfile) {
        updateProfileFields(userProfile)
      }
    }
  })

  profileForm.addEventListener('submit', function (event) {
    event.preventDefault()
    // Update displayed profile information
    displayName.textContent = profileForm.name.value
    displayEmail.textContent = profileForm.email.value
    displayDescription.textContent = profileForm.description.value

    // Save updated profile information to localStorage
    const updatedProfile = {
      firstName: profileForm.name.value.split(' ')[0],
      lastName: profileForm.name.value.split(' ')[1],
      email: profileForm.email.value,
      description: profileForm.description.value,
      phoneNumber: profileForm.phoneCountryCode.value + profileForm.phone.value,
      country: profileForm.country.value,
      city: profileForm.city.value
    }
    makeHttpRequest('/users/' + userProfile.id, 'PUT', updatedProfile)

    editProfileForm.classList.add('hidden')
  })

  profilePic.addEventListener('click', function () {
    profilePicInput.click()
  })

  profilePicInput.addEventListener('change', function (event) {
    const file = event.target.files[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = function (e) {
        profilePic.src = e.target.result
      }
      reader.readAsDataURL(file)

      const formData = new FormData()
      formData.append('file', file)
      makeXHRRequest('/users/updateProfilePicture', formData)
    }
  })

  async function fetchUserInterests() {
    try {
      const apiUrl = '/userinterests/user'
      const response = await makeHttpRequest(apiUrl, 'GET')

      response.forEach(interest => {
        const li = createInterestElement(interest.interest)
        displayInterests.appendChild(li)
      })
    } catch (error) {
      console.error('Error fetching user interests:', error)
    }
  }

  addInterestButton.addEventListener('click', function () {
    const interest = musicalInterestSelect.value
    addInterestToBackend(interest)
  })

  addCustomInterestButton.addEventListener('click', function () {
    customInterestInput.classList.toggle('hidden')
    if (!customInterestInput.classList.contains('hidden')) {
      customInterestInput.focus()
    }
  })

  customInterestInput.addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
      const interest = customInterestInput.value.trim()
      if (interest) {
        addInterestToBackend(interest)
        customInterestInput.value = ''
        customInterestInput.classList.add('hidden')
      }
    }
  })

  async function addInterestToBackend(interest) {
    try {
      const apiUrl = '/userinterests'
      const response = await makeHttpRequest(apiUrl, 'POST', { interest: interest })

      if (response.status === 201) {
        const addedInterest = interest
        const li = createInterestElement(addedInterest)
        displayInterests.appendChild(li)
      } else {
        console.error('Failed to add interest:', response.statusText)
      }
    } catch (error) {
      console.error('Error adding interest:', error)
    }
  }

  function createInterestElement(interest) {
    const li = document.createElement('li')
    li.className = 'interest'
    li.textContent = interest

    const removeButton = document.createElement('button')
    removeButton.className = 'remove-interest'
    removeButton.textContent = 'x'
    removeButton.addEventListener('click', function () {
      removeInterestFromBackend(interest)
      li.remove()
    })

    li.appendChild(removeButton)
    return li
  }

  async function removeInterestFromBackend(interest) {
    try {
      const encodedInterest = encodeURIComponent(interest)
      const apiUrl = `/userinterests/${encodedInterest}` // Endpoint to delete user interest on the backend
      const response = await makeHttpRequest(apiUrl, 'DELETE')

      if (response.status === 200) {
        console.log('Interest removed successfully')
      } else {
        console.error('Failed to remove interest:', response.statusText)
      }
    } catch (error) {
      console.error('Error removing interest:', error)
    }
  }

  searchFriendsButton.addEventListener('click', function () {
    const query = friendSearch.value.trim().toLowerCase()
    searchFriends(query)
  })

  function searchFriends(query) {
    friendsSearchResults.innerHTML = ''
    if (query) {
      const results = Array.from(myFriendsList.children)
      results.forEach(function (friend) {
        const friendName = friend.textContent.trim().toLowerCase()
        if (friendName.includes(query)) {
          const li = document.createElement('li')
          li.textContent = friend.textContent
          friendsSearchResults.appendChild(li)
        }
      })
    }
  }

  document.getElementById('logoutButton').addEventListener('click', function () {
    localStorage.removeItem('userProfile')
    localStorage.removeItem('user')
    window.location.href = '/notebridge/login'
  })

  document.getElementById('backButton').addEventListener('click', function () {
    window.location.href = '/notebridge/'
  })
})
