export const environment = {
  production: true,
  api: {
    baseUrl: '/api',
  },
  config: {
    url: 'http://localhost:8085',
    realm: 'medflow',
    clientId: 'medflow-frontend',
    resourceId: 'medflow-backend',
  },
  initOptions: {
    onLoad: 'login-required',
    checkLoginIframe: false,
    pkceMethod: 'S256',
  },
} as const;
