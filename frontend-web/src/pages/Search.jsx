import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Search as SearchIcon, Plus } from 'lucide-react';
import api from '../api/client';

export default function Search() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      // The endpoint defined in animeController is /api/anime/search?query=...
      const res = await api.get(`/api/anime/search?query=${query}`);
      setResults(res.data.data.animes || []);
    } catch (err) {
      console.error(err);
      alert('Erro na busca');
    } finally {
      setLoading(false);
    }
  };

  const handleAddAnime = async (animeId) => {
    try {
      const username = prompt("Digite seu username para confirmar:");
      if (!username) return;
      
      await api.post('/api/anime/user/add', {
        username: username,
        animeId: animeId.toString(),
        status: 'WATCHING'
      });
      alert('Anime adicionado! Verifique seu Feed de atividades.');
      navigate('/');
    } catch (err) {
      console.error(err);
      alert('Erro ao adicionar anime. Verifique se ele já não está na sua lista.');
    }
  };

  return (
    <div className="page-container">
      <header style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '32px' }}>
        <button onClick={() => navigate('/')} style={{ background: 'none', border: 'none', color: 'var(--text-primary)' }}>
          <ArrowLeft size={24} />
        </button>
        <h1 style={{ fontSize: '2rem' }}>Explorar Animes</h1>
      </header>

      <form onSubmit={handleSearch} style={{ display: 'flex', gap: '12px', marginBottom: '32px' }}>
        <div style={{ position: 'relative', flex: 1 }}>
          <SearchIcon style={{ position: 'absolute', top: '12px', left: '16px', color: 'var(--text-secondary)' }} size={20} />
          <input 
            type="text" 
            placeholder="Pesquisar por nome (ex: Naruto, Bleach...)" 
            className="input-field" 
            style={{ paddingLeft: '48px' }}
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            required 
          />
        </div>
        <button type="submit" className="btn-primary" disabled={loading}>
          {loading ? 'Buscando...' : 'Pesquisar'}
        </button>
      </form>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '24px' }}>
        {results.map(anime => (
          <div key={anime.id} className="glass-panel" style={{ overflow: 'hidden', display: 'flex', flexDirection: 'column' }}>
            <div style={{ height: '280px', backgroundColor: 'var(--surface-color)' }}>
              {anime.imageUrl ? (
                <img src={anime.imageUrl} alt={anime.title} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
              ) : (
                <div style={{ width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--text-secondary)' }}>Sem Imagem</div>
              )}
            </div>
            <div style={{ padding: '16px', flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <h4 style={{ marginBottom: '12px', fontSize: '1rem' }}>{anime.title}</h4>
              <button onClick={() => handleAddAnime(anime.id)} className="btn-primary" style={{ width: '100%', fontSize: '0.9rem', padding: '8px' }}>
                <Plus size={16} /> Adicionar à Lista
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
