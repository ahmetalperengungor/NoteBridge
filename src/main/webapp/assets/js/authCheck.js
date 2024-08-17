// send request to "/api/users/me" to check if user is logged in every time the page is loaded with jquery
// if user is not logged in, redirect to login page
// if user is logged in, show the page
//
$(document).ready(function () {
  console.log('origin', window.location.origin)
  let token = localStorage.getItem('user')
  if (!token) {
    window.location.href = '/notebridge/login'
    return
  }

  fetch('/notebridge/api/users/me', {
    method: 'GET',
    headers: {
      Authorization: 'Bearer ' + token.substring(1, token.length - 1)
    }
  })
    .then(response => response.json())
    .then(data => {
      localStorage.setItem('userProfile', JSON.stringify(data))

      if (!data) {
        window.location.href = '/notebridge/login'
      }
    })
    .catch(() => {
      window.location.href = '/notebridge/login'
    })
})
