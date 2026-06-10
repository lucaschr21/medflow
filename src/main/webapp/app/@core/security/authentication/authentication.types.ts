import { KeycloakTokenParsed } from 'keycloak-js';

export type AuthenticationTokenParsed = Readonly<
  KeycloakTokenParsed & {
    preferred_username?: string;
    email?: string;
    name?: string;
    given_name?: string;
    family_name?: string;
    groups?: readonly string[];
  }
>;
