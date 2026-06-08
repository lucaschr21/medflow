export const environment = {
  production: false,

  api: {
    baseUrl: '/api',
  },

  authentication: {
    config: {
      url: 'http://localhost:8085',
      realm: 'medflow',
      clientId: 'medflow-frontend',
    },

    initOptions: {
      onLoad: 'login-required',
      checkLoginIframe: false,
      pkceMethod: 'S256',
    },

    bearerTokenUrlPattern: /^http:\/\/localhost:(?:8080|8085)(?:\/.*)?$/i,
  },

  authorization: {
    resourceId: 'medflow-backend',
  },
} as const;
