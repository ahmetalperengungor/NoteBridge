// JavaScript to handle the pop-up form display
const signupLink = document.getElementById('signup-link')
const signupForm = document.getElementById('signup-form')
const overlay = document.getElementById('overlay')
const closeBtn = document.getElementById('close-btn')
const signupSubmit = document.getElementById('signup-submit')

signupLink.addEventListener('click', e => {
  e.preventDefault()
  signupForm.style.display = 'block'
  overlay.style.display = 'block'
})

closeBtn.addEventListener('click', () => {
  signupForm.style.display = 'none'
  overlay.style.display = 'none'
})

overlay.addEventListener('click', () => {
  signupForm.style.display = 'none'
  overlay.style.display = 'none'
})

// Function to toggle password visibility
function togglePasswordVisibility(inputId) {
  const passwordInput = document.getElementById(inputId)
  if (passwordInput.type === 'password') {
    passwordInput.type = 'text'
  } else {
    passwordInput.type = 'password'
  }
}

// Handle input label transitions
const inputBoxes = document.querySelectorAll('.popup-form .form .inputBox')
inputBoxes.forEach(inputBox => {
  const input = inputBox.querySelector('input')
  input.addEventListener('input', () => {
    if (input.value !== '') {
      inputBox.querySelector('i').style.top = '-8px'
      inputBox.querySelector('i').style.fontSize = '0.8em'
      inputBox.querySelector('i').style.color = '#888'
    } else {
      inputBox.querySelector('i').style.top = '50%'
      inputBox.querySelector('i').style.fontSize = '1em'
      inputBox.querySelector('i').style.color = '#aaa'
    }
  })
})

signupSubmit.addEventListener('click', e => {
  e.preventDefault()
  const firstName = document.getElementById('first-name').value
  const lastName = document.getElementById('last-name').value
  const email = document.getElementById('email').value
  const phoneCountryCode = document.getElementById('phone-country-code').value
  const phoneNumber = document.getElementById('phone-number').value
  const username = document.getElementById('signup-username').value
  const password = document.getElementById('signup-password').value
  const confirmPassword = document.getElementById('signup-confirm-password').value

  // Validate form fields
  // Form validation checks
  if (!firstName.trim()) {
    alert('Please fill in your First Name.')
    return
  }

  if (!lastName.trim()) {
    alert('Please fill in your Last Name.')
    return
  }

  if (!email.includes('@') || !email.includes('.com')) {
    alert('Please enter a valid Email address.')
    return
  }

  if (!phoneCountryCode) {
    alert('Please select a Country Code.')
    return
  }

  if (!phoneNumber.trim() || isNaN(phoneNumber)) {
    alert('Please enter a valid Phone Number.')
    return
  }

  if (!password.trim() || password !== confirmPassword) {
    alert('Passwords do not match.')
    return
  }

  var body = {
    gender: 0,
    email: email,
    password: password,
    firstName: firstName,
    lastName: lastName,
    birthDate: '1990-01-01',
    phoneNumber: phoneCountryCode + phoneNumber
  }

  let url = '/notebridge/api/users/signup'
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
    .then(response => (window.location.href = '/notebridge/login'))

    .catch(error => {
      alert('Signup failed, please check your email and password')
    })
})
