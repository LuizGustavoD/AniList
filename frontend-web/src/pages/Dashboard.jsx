import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Bell, Search, MessageSquare, LogOut, Activity, User } from 'lucide-react';
import api from '../api/client';

export default function Dashboard() {
  const [feed, setFeed] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    loadFeed();
    loadNotifications();
  }, []);

  const loadFeed = async () => {
    try {
      const res = await api.get('/api/feed?page=0&size=20');
      setFeed(res.data.data.content || []);
    } catch (err) {
      console.error(err);
    }
  };

  const loadNotifications = async () => {
    try {
      const res = await api.get('/api/notifications');
      setNotifications(res.data.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/auth');
  };

  return (
    <div className="page-container">
      {/* Header Premium */}
      <header className="glass-panel" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '16px 24px', marginBottom: '32px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: 'linear-gradient(135deg, var(--primary-color), var(--accent-color))', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Activity color="white" />
          </div>
          <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>AniSocial</h1>
        </div>
        
        <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
          <button style={{ background: 'none', border: 'none', color: 'var(--text-primary)' }} onClick={() => navigate('/search')}><Search /></button>
          <button style={{ background: 'none', border: 'none', color: 'var(--text-primary)' }} onClick={() => navigate('/forums')}><MessageSquare /></button>
          
          <div style={{ position: 'relative' }}>
            <button style={{ background: 'none', border: 'none', color: 'var(--text-primary)', position: 'relative' }} onClick={() => setShowNotifications(!showNotifications)}>
              <Bell />
              {notifications.filter(n => !n.isRead).length > 0 && (
                <span style={{ position: 'absolute', top: '-5px', right: '-5px', background: 'var(--danger-color)', width: '10px', height: '10px', borderRadius: '50%' }}></span>
              )}
            </button>
            
            {showNotifications && (
              <div className="glass-panel" style={{ position: 'absolute', right: '0', top: '40px', width: '300px', padding: '16px', zIndex: 10 }}>
                <h3 style={{ marginBottom: '12px' }}>Notificações</h3>
                {notifications.length === 0 ? <p style={{ color: 'var(--text-secondary)' }}>Nenhuma notificação</p> : 
                  notifications.map(n => (
                    <div key={n.id} style={{ padding: '8px 0', borderBottom: '1px solid var(--glass-border)' }}>
                      <p style={{ fontSize: '0.9rem' }}>{n.content}</p>
                    </div>
                  ))
                }
              </div>
            )}
          </div>
          
          <button onClick={handleLogout} style={{ background: 'none', border: 'none', color: 'var(--danger-color)' }}><LogOut /></button>
        </div>
      </header>

      {/* Main Feed Content */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 300px', gap: '24px' }}>
        <main>
          <h2 style={{ marginBottom: '24px' }}>Feed de Atividades</h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {feed.length === 0 ? (
              <div className="glass-panel" style={{ padding: '32px', textAlign: 'center', color: 'var(--text-secondary)' }}>
                Seu feed está vazio. Adicione amigos ou animes para ver atividades!
              </div>
            ) : (
              feed.map(activity => (
                <div key={activity.id} className="glass-panel" style={{ padding: '20px', transition: 'transform 0.2s' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '12px' }}>
                    <div style={{ width: '40px', height: '40px', borderRadius: '50%', backgroundColor: 'var(--surface-color)', overflow: 'hidden' }}>
                      {activity.userProfilePicture ? <img src={activity.userProfilePicture} alt="" style={{ width: '100%', height: '100%', objectFit: 'cover' }}/> : <User color="var(--text-secondary)" style={{ margin: '10px' }}/>}
                    </div>
                    <div>
                      <h4 style={{ color: 'var(--primary-color)' }}>{activity.username}</h4>
                      <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{new Date(activity.createdAt).toLocaleString()}</span>
                    </div>
                  </div>
                  <p>
                    {activity.activityType === 'ADDED_ANIME' && `Adicionou o anime #${activity.referenceId} à lista.`}
                    {activity.activityType === 'REVIEWED_ANIME' && `Escreveu uma review para o anime #${activity.referenceId}.`}
                    {activity.activityType === 'COMPLETED_ANIME' && `Completou o anime #${activity.referenceId}.`}
                  </p>
                </div>
              ))
            )}
          </div>
        </main>
        
        {/* Sidebar Recomendações */}
        <aside>
          <div className="glass-panel" style={{ padding: '20px', position: 'sticky', top: '24px' }}>
            <h3 style={{ marginBottom: '16px', color: 'var(--accent-color)' }}>Recomendados para Você</h3>
            {/* Aqui poderiamos buscar da api/recommendations */}
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Conecte-se com mais amigos para ver recomendações personalizadas baseadas nos seus gêneros favoritos.</p>
          </div>
        </aside>
      </div>
    </div>
  );
}
