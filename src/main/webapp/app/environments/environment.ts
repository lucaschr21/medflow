export const environment = {
  production: true,
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
