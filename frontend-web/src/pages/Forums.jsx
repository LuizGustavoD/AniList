import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, MessageCircle, Plus } from 'lucide-react';
import api from '../api/client';

export default function Forums() {
  const [threads, setThreads] = useState([]);
  const [showCreate, setShowCreate] = useState(false);
  const [newThread, setNewThread] = useState({ title: '', content: '' });
  const navigate = useNavigate();

  useEffect(() => {
    loadThreads();
  }, []);

  const loadThreads = async () => {
    try {
      const res = await api.get('/api/forum/threads?page=0&size=20');
      setThreads(res.data.data.content || []);
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreateThread = async (e) => {
    e.preventDefault();
    try {
      await api.post('/api/forum/thread', newThread);
      setShowCreate(false);
      setNewThread({ title: '', content: '' });
      loadThreads();
    } catch (err) {
      console.error(err);
      alert('Erro ao criar tópico');
    }
  };

  return (
    <div className="page-container">
      <header style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '32px' }}>
        <button onClick={() => navigate('/')} style={{ background: 'none', border: 'none', color: 'var(--text-primary)' }}>
          <ArrowLeft size={24} />
        </button>
        <h1 style={{ fontSize: '2rem' }}>Fóruns da Comunidade</h1>
      </header>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '24px' }}>
        <button className="btn-primary" onClick={() => setShowCreate(!showCreate)}>
          <Plus size={20} /> Novo Tópico
        </button>
      </div>

      {showCreate && (
        <div className="glass-panel" style={{ padding: '24px', marginBottom: '24px' }}>
          <h3>Criar Novo Tópico</h3>
          <form onSubmit={handleCreateThread} style={{ display: 'flex', flexDirection: 'column', gap: '16px', marginTop: '16px' }}>
            <input 
              type="text" 
              placeholder="Título do Tópico" 
              className="input-field"
              value={newThread.title}
              onChange={(e) => setNewThread({...newThread, title: e.target.value})}
              required 
            />
            <textarea 
              placeholder="Conteúdo" 
              className="input-field" 
              rows="4"
              value={newThread.content}
              onChange={(e) => setNewThread({...newThread, content: e.target.value})}
              required 
            />
            <button type="submit" className="btn-primary" style={{ alignSelf: 'flex-end' }}>Publicar</button>
          </form>
        </div>
      )}

      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        {threads.length === 0 ? (
          <div className="glass-panel" style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}>
            Nenhum tópico criado ainda. Seja o primeiro!
          </div>
        ) : (
          threads.map(thread => (
            <div key={thread.id} className="glass-panel" style={{ padding: '20px', display: 'flex', gap: '16px' }}>
              <div style={{ padding: '16px', backgroundColor: 'var(--surface-color)', borderRadius: '12px', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <MessageCircle color="var(--primary-color)" />
                <span style={{ fontSize: '0.8rem', marginTop: '4px' }}>{thread.comments?.length || 0}</span>
              </div>
              <div style={{ flex: 1 }}>
                <h3 style={{ color: 'var(--text-primary)', marginBottom: '8px' }}>{thread.title}</h3>
                <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '12px' }}>{thread.content}</p>
                <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                  Por <span style={{ color: 'var(--accent-color)' }}>{thread.authorName}</span> • {new Date(thread.createdAt).toLocaleDateString()}
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
