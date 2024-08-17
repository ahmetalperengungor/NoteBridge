let SERVER_BASE_URL = '/notebridge/api'

const makeHttpRequest = async (url, method = 'GET', body) => {
  let token = localStorage.getItem('user')
  const combinedUrl = SERVER_BASE_URL + url
  if (token) {
    let response = await fetch(combinedUrl, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token.replace(/['"]+/g, '')
      },
      body: JSON.stringify(body)
    })
    if (response.status === 401) {
      window.location.href = '/notebridge/login'
    } else if (response.status === 201) {
      return response
    } else if (response.status === 200) {
      try {
        return response.json()
      } catch (error) {
        return response
      }
    } else {
      return response
    }
  } else {
    window.location.href = '/notebridge/login'
  }
}

const makeXHRRequest = async (url, body) => {
  let token = localStorage.getItem('user')
  const combinedUrl = SERVER_BASE_URL + url
  if (token) {
    var xhr = new XMLHttpRequest()
    xhr.open('POST', combinedUrl, true)
    xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem('user').replace(/['"]+/g, ''))

    xhr.onload = function () {
      if (xhr.status === 200) {
        return true
      } else {
        throw new Error(xhr.responseText)
      }
    }

    xhr.send(body)
  } else {
    window.location.href = '/notebridge/login'
  }
}
