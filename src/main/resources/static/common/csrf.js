let cachedCsrfToken = null;
async function fetchCsrfToken() {
    // 이미 캐시된 CSRF 토큰이 있으면 반환
    if (cachedCsrfToken) {
        console.log("캐시된 CSRF 토큰 사용:", cachedCsrfToken);
        return cachedCsrfToken;
    }

    try {
        const response = await fetch('/csrf-token', {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            const csrfToken = data.csrfToken;
            console.log("새로운 CSRF 토큰을 성공적으로 가져왔습니다:", csrfToken);

            // 캐시에 저장
            cachedCsrfToken = csrfToken;
            return csrfToken;
        } else {
            alert("CSRF 토큰을 가져오지 못했습니다.");
            return null;
        }
    } catch (error) {
        console.error("CSRF 토큰 요청 실패:", error);
        alert("CSRF 토큰 요청 실패: 서버와의 통신 중 문제가 발생했습니다.");
        return null;
    }
}


function clearCsrfToken() {
    cachedCsrfToken = null;
    console.log("CSRF 토큰 캐시 초기화");
}

