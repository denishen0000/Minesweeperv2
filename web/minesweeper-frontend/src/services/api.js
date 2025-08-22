

const API = {
  post: async (url) => {
    const res = await fetch(url, { method: 'POST' });
    return res.json();
  },
  get: async (url) => {
    const res = await fetch(url);
    return res.json();
  }
};

export const newGame = (size, bombs) => API.post(`/game/new?size=${size}&bombs=${bombs}`);
export const revealCell = (x, y) => API.post(`/game/reveal?row=${x}&col=${y}`);
export const flagCell = (x, y) => API.post(`/game/flag?row=${x}&col=${y}`);
export const getStatus = () => API.get(`/game/status`);
