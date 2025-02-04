import React, { useState } from'react';

function Register(){
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleRegister = async (e)=>{
        e.preventDefault();

        try{
            const response = await fetch('http://localhost:8080/api/user/register',{
                method : 'POST',
                headers:{
                    'Content-Type':'application/json',
                },
                body: JSON.stringify({username,password}),
            });

            const data = await response.text();
            setMessage(data);
        }catch (error){
            console.error('Error:',error);
            setMessage('회원가입 실패');
        }
    };

    return(
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleRegister}>
                <input
                    type="text"
                    placeholder="아이디"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">가입하기</button>
            </form>
            <p>{message}</p>
        </div>
    );
}

export default Register;