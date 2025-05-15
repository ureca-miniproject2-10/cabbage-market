// HTML 요소를 로드하는 함수
function loadHTMLComponent(url, elementId) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            document.getElementById(elementId).innerHTML = data;

            // navbar가 로드된 후 로그인 상태에 따라 UI 업데이트
            if (elementId === 'navbar-container') {
                checkAuthStatus();
            }
        })
        .catch(error => {
            console.error('컴포넌트 로드 중 오류 발생:', error);
        });
}

// 서버에서 인증 상태 확인
function checkAuthStatus() {
    fetch('/auth/me', {
        method: 'GET',
        credentials: 'include', // 쿠키 포함
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            updateNavbarUI(data);
        })
        .catch(error => {
            console.error('인증 상태 확인 중 오류 발생:', error);
            // 오류 발생 시 비로그인 상태로 처리
            updateNavbarUI({ status: 'unauthenticated' });
        });
}

function updateNavbarUI(userData) {
    const authButtons = document.getElementById('auth-buttons');
    if (!authButtons) return;

    if (userData.status === 'authenticated') {
        const profileImageUrl = userData.profileImageUrl || '/images/default_user.png';
        const isAdmin = userData.isAdmin === 'true';

        let adminButton = '';
        if (isAdmin) {
            adminButton = `<a href="/admin.html" class="nav-btn admin-btn">관리자 페이지</a>`;
        }

        // authButtons.innerHTML을 먼저 설정
        authButtons.innerHTML = `
            <div class="user-profile">
                <div class="profile-image-container">
                    <img src="${profileImageUrl}" alt="프로필" class="profile-image">
                </div>
                <span class="username">${userData.name}</span>
                ${adminButton}
                <a href="/mypage.html" class="nav-btn mypage-btn">마이페이지</a>
                <button id="logout-btn" class="nav-btn logout-btn">로그아웃</button>
            </div>
        `;

        requestAnimationFrame(() => {
            const logoutBtn = document.getElementById('logout-btn');
            if (logoutBtn) {
                logoutBtn.addEventListener('click', function (e) {
                    e.preventDefault();
                    logout();
                });
            }
        });
    } else {
        authButtons.innerHTML = `
            <a href="/login.html" class="nav-btn login-btn">로그인</a>
            <a href="/register.html" class="nav-btn signup-btn">회원가입</a>
        `;
    }
}

async function logout() {
    try {
        // CSRF 토큰 가져오기
        const csrfToken = await fetchCsrfToken();

        if (!csrfToken) {
            alert("CSRF 토큰이 설정되지 않았습니다. 페이지를 새로고침 해주세요.");
            return;
        }

        const response = await fetch('/auth/logout', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': csrfToken
            }
        });

        if (response.ok) {
            alert("로그아웃 성공");
            clearCsrfToken();  // CSRF 토큰 캐시 초기화
            window.location.href = "/";
        } else {
            alert("로그아웃 실패");
        }
    } catch (error) {
        console.error("로그아웃 요청 실패:", error);
        alert("로그아웃 요청 실패");
    }
}

// 로그인 함수
function loginUser(username, password) {
    const loginData = {
        username: username,
        password: password
    };

    fetch('/login', {
        method: 'POST',
        credentials: 'include', // 쿠키 포함
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('로그인 실패');
            }
        })
        .then(data => {
            alert('로그인 성공!');
            window.location.href = '/'; // 홈페이지로 리다이렉트
        })
        .catch(error => {
            console.error('로그인 중 오류 발생:', error);
            alert('로그인 실패. 아이디와 비밀번호를 확인해주세요.');
        });
}

// 페이지 로드 시 navbar와 footer 로드
document.addEventListener('DOMContentLoaded', function() {
    // navbar 로드
    if (document.getElementById('navbar-container')) {
        loadHTMLComponent('/common/navbar.html', 'navbar-container');
    }

    // footer 로드
    if (document.getElementById('footer-container')) {
        loadHTMLComponent('/common/footer.html', 'footer-container');
    }

    // 검색 기능
    setTimeout(() => {
        // 로그인 폼 이벤트 리스너
        const loginForm = document.querySelector('.login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', function(e) {
                e.preventDefault();
                const username = document.getElementById('username').value;
                const password = document.getElementById('password').value;

                loginUser(username, password);
            });
        }
    }, 100); // 컴포넌트가 로드된 후 이벤트 리스너 추가를 위한 짧은 지연
});

// 로그인 여부
async function isLoggedIn() {
    try {
        const response = await fetch('/auth/me', {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        return response.ok; // 200이면 로그인 상태, 아니면 false
    } catch (error) {
        console.error('로그인 상태 확인 실패:', error);
        return false;
    }
}

// modal.js
function showModal(message) {
    const modal = document.getElementById("customModal");
    const messageBox = document.getElementById("modalMessage");
    if (modal && messageBox) {
        messageBox.innerText = message;
        modal.classList.remove("hidden");
    }
}

function closeModal() {
    const modal = document.getElementById("customModal");
    if (modal) modal.classList.add("hidden");
}

