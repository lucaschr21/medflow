package br.com.medflow.core.security.identity;

import java.util.Set;

/**
 * Porta de saída para operações de gerenciamento de identidade
 * no provedor externo (Keycloak).
 */
public interface IdentityProviderAdminPort {

        /**
         * Cria um usuário no provedor de identidade e retorna o identificador externo.
         *
         * @param username  nome de usuário
         * @param email     e-mail
         * @param firstName nome
         * @param lastName  sobrenome
         * @param groups    grupos (roles) a atribuir
         * @return identificador externo gerado
         */
        String criarUsuario(String username, String email, String firstName,
                        String lastName, Set<String> groups);

        /**
         * Atualiza os dados básicos de um usuário.
         */
        void atualizarUsuario(String keycloakId, String username, String email,
                        String firstName, String lastName);

        /**
         * Desabilita um usuário (inativação).
         */
        void desabilitarUsuario(String keycloakId);

        /**
         * Habilita um usuário.
         */
        void habilitarUsuario(String keycloakId);
}
