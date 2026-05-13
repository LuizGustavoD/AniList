import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Mail, Lock, User, ArrowRight } from 'lucide-react';
import api from '../api/client';

export default function Auth() {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({ username: '', password: '', email: '' });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isLogin) {
        const res = await api.post('/api/auth/login', {
          username: formData.username,
          password: formData.password
        });
        localStorage.setItem('token', res.data.data.token);
        navigate('/');
      } else {
        await api.post('/api/auth/register', {
          username: formData.username,
          email: formData.email,
          password: formData.password
        });
        setIsLogin(true);
      }
    } catch (err) {
      alert('Erro na autenticação!');
      console.error(err);
    }
  };

  return (
    <div className="page-container" style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div className="glass-panel" style={{ width: '100%', maxWidth: '400px', padding: '40px' }}>
        <h2 style={{ textAlign: 'center', marginBottom: '32px', fontSize: '2rem', background: 'linear-gradient(to right, var(--primary-color), var(--accent-color))', WebkitBackgroundClip: 'text', color: 'transparent' }}>
          {isLogin ? 'Bem-vindo de volta' : 'Junte-se a nós'}
        </h2>
        
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <div style={{ position: 'relative' }}>
            <User style={{ position: 'absolute', top: '12px', left: '16px', color: 'var(--text-secondary)' }} size={20} />
            <input 
              type="text" 
              placeholder="Username" 
              className="input-field" 
              style={{ paddingLeft: '48px' }}
              value={formData.username}
              onChange={(e) => setFormData({...formData, username: e.target.value})}
              required 
            />
          </div>

          {!isLogin && (
            <div style={{ position: 'relative' }}>
              <Mail style={{ position: 'absolute', top: '12px', left: '16px', color: 'var(--text-secondary)' }} size={20} />
              <input 
                type="email" 
                placeholder="Email" 
                className="input-field" 
                style={{ paddingLeft: '48px' }}
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
                required 
              />
            </div>
          )}

          <div style={{ position: 'relative' }}>
            <Lock style={{ position: 'absolute', top: '12px', left: '16px', color: 'var(--text-secondary)' }} size={20} />
            <input 
              type="password" 
              placeholder="Senha" 
              className="input-field" 
              style={{ paddingLeft: '48px' }}
              value={formData.password}
              onChange={(e) => setFormData({...formData, password: e.target.value})}
              required 
            />
          </div>

          <button type="submit" className="btn-primary" style={{ marginTop: '12px' }}>
            {isLogin ? 'Entrar' : 'Registrar'} <ArrowRight size={20} />
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '24px', color: 'var(--text-secondary)' }}>
          {isLogin ? 'Não tem uma conta?' : 'Já tem uma conta?'}
          <button 
            type="button" 
            style={{ background: 'none', border: 'none', color: 'var(--primary-color)', marginLeft: '8px', fontWeight: '600' }}
            onClick={() => setIsLogin(!isLogin)}
          >
            {isLogin ? 'Registre-se' : 'Faça login'}
          </button>
        </div>
      </div>
    </div>
  );
}
