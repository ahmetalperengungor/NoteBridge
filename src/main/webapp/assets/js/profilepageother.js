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
  userProfile = JSON.parse(localStorage.getItem('userProfile'))

  // const username = window.location.pathname.split('/').pop()

  if (username === userProfile.email) {
    window.location.href = '/notebridge/profile'
  }

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

  async function fetchUserInterests() {
    try {
      const apiUrl = '/userinterests/user/' + username
      const response = await makeHttpRequest(apiUrl, 'GET')

      response.forEach(interest => {
        const li = createInterestElement(interest.interest)
        displayInterests.appendChild(li)
      })
    } catch (error) {
      console.error('Error fetching user interests:', error)
    }
  }

  function createInterestElement(interest) {
    const li = document.createElement('li')
    li.className = 'interest'
    li.textContent = interest

    return li
  }

  document.getElementById('backButton').addEventListener('click', function () {
    window.location.href = '/notebridge/'
  })
})
