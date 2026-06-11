package br.com.medflow.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.exceptions.ErrorCode;
import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.Usuario;
import br.com.medflow.repositories.MedicoRepository;
import br.com.medflow.repositories.UsuarioRepository;

/**
 * Resolve o contexto local do usuário autenticado.
 *
 * <p>
 * Traduz o {@link AuthenticatedUser} (identidade externa do Keycloak) para as
 * entidades locais
 * {@link Usuario} e {@link Medico}.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioContextService {

    private final CurrentAuthenticatedUser currentUser;
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;

    public UsuarioContextService(
            CurrentAuthenticatedUser currentUser,
            UsuarioRepository usuarioRepository,
            MedicoRepository medicoRepository) {
        this.currentUser = currentUser;
        this.usuarioRepository = usuarioRepository;
        this.medicoRepository = medicoRepository;
    }

    /**
     * Retorna o {@link Usuario} local vinculado ao usuário autenticado.
     *
     * @return usuário local
     * @throws BusinessRuleException se o usuário não tiver vínculo local ou estiver
     *                               inativo
     */
    public Usuario getUsuarioOuFalha() {
        AuthenticatedUser authUser = currentUser.getRequired();
        UUID keycloakId = UUID.fromString(authUser.subject());
        Usuario usuario = usuarioRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(
                        () -> new BusinessRuleException(
                                ErrorCode.USER_NOT_LINKED,
                                "Usuário autenticado não possui vínculo local no Medflow."));
        if (!usuario.isAtivo()) {
            throw new BusinessRuleException(ErrorCode.USER_INACTIVE, "Usuário está inativo no Medflow.");
        }
        return usuario;
    }

    /**
     * Retorna o {@link Medico} vinculado ao usuário autenticado, se existir.
     *
     * @return médico opcional
     */
    public Optional<Medico> getMedico() {
        Usuario usuario = getUsuarioOuFalha();
        return medicoRepository.findByUsuarioId(usuario.getId());
    }

    /**
     * Retorna o {@link Medico} vinculado ao usuário autenticado.
     *
     * @return médico
     * @throws EntityNotFoundException se o usuário não for médico
     */
    public Medico getMedicoOuFalha() {
        return getMedico()
                .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não é um médico."));
    }
}
