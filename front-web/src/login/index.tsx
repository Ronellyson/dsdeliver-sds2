import React, { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import './styles.css';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const history = useHistory();

    const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (email === 'admin@gmail.com' && password === '123') {
            console.log('Usuário autenticado com sucesso!');
            // Redirecionar para a página de gerenciamento de produtos após o login bem-sucedido
            history.push('/product-management');
        } else {
            console.log('Credenciais inválidas. Tente novamente.');
        }
    };

    return (
        <div className="login-container">
            <div className="login-content">
                <h1 className="login-title">Faça Login</h1>
                <form onSubmit={handleLogin} className="login-form">
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Senha:</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="login-btn">
                        Entrar
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;
