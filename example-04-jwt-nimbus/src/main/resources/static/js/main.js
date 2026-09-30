const tokenKey = 'gocSachJwt';

function showMessage(element, text, error = false) {
  if (!element) return;
  element.textContent = text;
  element.className = `message${error ? ' error' : ''}`;
  element.style.display = 'block';
}

async function request(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: {'Content-Type': 'application/json', ...(options.headers || {})}
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(data.detail || 'Không thể hoàn thành yêu cầu.');
  return data;
}

document.querySelector('#loginForm')?.addEventListener('submit', async event => {
  event.preventDefault();
  const message = document.querySelector('#loginMessage');
  try {
    const data = await request('/auth/login', {method: 'POST', body: JSON.stringify({
      email: document.querySelector('#loginEmail').value,
      password: document.querySelector('#loginPassword').value
    })});
    sessionStorage.setItem(tokenKey, data.token);
    window.location.href = '/profile';
  } catch (error) {
    showMessage(message, error.message, true);
  }
});

document.querySelector('#registerForm')?.addEventListener('submit', async event => {
  event.preventDefault();
  const message = document.querySelector('#registerMessage');
  try {
    await request('/auth/signup', {method: 'POST', body: JSON.stringify({
      fullName: document.querySelector('#fullName').value,
      email: document.querySelector('#registerEmail').value,
      password: document.querySelector('#registerPassword').value
    })});
    showMessage(message, 'Đăng ký thành công. Bạn có thể đăng nhập ngay.');
    event.target.reset();
  } catch (error) {
    showMessage(message, error.message, true);
  }
});

async function loadProfile() {
  const token = sessionStorage.getItem(tokenKey);
  const message = document.querySelector('#profileMessage');
  if (!token) {
    window.location.href = '/';
    return;
  }
  try {
    const user = await request('/users/me', {headers: {Authorization: `Bearer ${token}`}});
    document.querySelector('#profileName').textContent = user.fullName;
    document.querySelector('#profileEmail').textContent = user.email;
    document.querySelector('#profileRole').textContent = user.role === 'ADMIN' ? 'Quản trị viên' : 'Người dùng';
    document.querySelector('#profile').hidden = false;
    showMessage(message, 'Đăng nhập thành công.');
  } catch (error) {
    sessionStorage.removeItem(tokenKey);
    showMessage(message, `${error.message} Vui lòng đăng nhập lại.`, true);
  }
}

document.querySelector('#reload')?.addEventListener('click', loadProfile);
document.querySelector('#logout')?.addEventListener('click', () => {
  sessionStorage.removeItem(tokenKey);
  window.location.href = '/';
});
if (document.querySelector('#profile')) loadProfile();
