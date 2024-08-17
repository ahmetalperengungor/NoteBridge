var submitButton = document.getElementById('loginButton')

submitButton.addEventListener('click', function () {
  var emailInput = document.getElementById('email-input')
  var passwordInput = document.getElementById('password-input')
  var email = emailInput.value
  var password = passwordInput.value

  if (!email || !password) {
    alert('Please enter your email and password')
    return
  }
  var body = {
    email: email,
    password: password
  }

  let url = '/notebridge/api/users/login'
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
    .then(response => response.json())
    .then(data => {
      if (data) {
        let token = data.data

        localStorage.setItem('user', JSON.stringify(token))
        window.location.href = '/notebridge/'
      } else {
        alert('Login failed, please check your email and password')
      }
    })
    .catch(error => {
      alert('Login failed, please check your email and password')
    })
})

// when document is ready
$(document).ready(function () {
  // check if user is logged in
  let token = localStorage.getItem('user')
  if (token) {
    $.ajax({
      url: '/notebridge/api/users/me',
      type: 'GET',
      headers: {
        Authorization: 'Bearer ' + token.substring(1, token.length - 1)
      },
      success: function (data) {
        if (data) {
          window.location.href = '/notebridge/'
        }
      },
      error: function () {}
    })
  }
})
