document.querySelector('#submit').addEventListener('click', ()=>{

    const email = document.querySelector(`#email`)
    const password = document.querySelector(`#password`)
    const username = document.querySelector(`#username`)

    fetch(`/signup`, {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify({
            email : email.value,
            password : password.value,
            username : username.value
        })
    })
        .then((resp) => {
            if(!resp.ok && resp.status !== 400){
                throw new Error("회원 등록 오류" + resp.status);
            }
            return resp.json();
        })
        .then(data => {
            alert("등록 성공")
            location.href="/"
        })
        .catch((error) => {
            alert("오류 발생 : " + error.message);
        });
})

document.querySelector('#back').addEventListener('click', ()=>{
    if(history.length>1){
        history.back()
    } else{
        location.href="/"
    }
})