export const environment = {
  production: false,
  api: {
    baseUrl: '/api',
  },
  config: {
    url: 'http://localhost:8085',
    realm: 'medflow',
    clientId: 'medflow-frontend',
    resourceId: 'medflow-backend',
  },
} as const;
