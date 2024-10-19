document.querySelector('#submit').addEventListener('click', (e) => {
    e.preventDefault();

    const username = document.querySelector(`#username`)
    const password = document.querySelector(`#password`)
    const formData = new URLSearchParams();
    formData.append('username', username.value);
    formData.append('password', password.value);
    fetch(`/login`, {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
        },
        credentials: 'include',
        body : formData.toString()
    })
        .then((resp) => {
            console.log(resp)
            if(!resp.ok && resp.status !== 400){
                console.log(resp.status)
                throw new Error("로그인 실패" + resp.status);
            } else{
                alert("로그인 성공!")
                location.href="/"
            }
        })
        .catch((error) => {
            console.error("fetch 에러:", error); // 전체 에러 객체 출력
            alert("로그인 오류 발생: " + error.message); // 사용자에게 오류 메시지 표시
        });

})


document.querySelector('#back').addEventListener('click', ()=>{
    if(history.length>1){
        history.back()
    } else{
        location.href="/"
    }
})