export const environment = {
  production: false,
  api: {
    baseUrl: '/api',
  },
  authentication: {
    url: 'http://localhost:8085',
    realm: 'medflow',
    clientId: 'medflow-frontend',
    backendAudience: 'medflow-backend',
  },
} as const;
