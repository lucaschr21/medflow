package br.com.medflow.core.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;
import jakarta.servlet.ServletException;

class RequestLoggingFilterTest {

  @Test
  void shouldLogApiRequestWithAuthenticatedUsername() throws ServletException, IOException {
    RequestLoggingFilter filter = new RequestLoggingFilter(currentUser("jane.doe"));
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/usuarios");
    MockHttpServletResponse response = new MockHttpServletResponse();

    filter.doFilter(request, response, new MockFilterChain());

    assertEquals(200, response.getStatus());
  }

  @Test
  void shouldKeepQueryStringInLoggedRequestLine() {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/usuarios");
    request.setQueryString("page=0&size=10");

    assertEquals("GET /api/usuarios?page=0&size=10", RequestLoggingFilter.requestLine(request));
  }

  @Test
  void shouldLogAnonymousWhenThereIsNoAuthenticatedUser() throws ServletException, IOException {
    RequestLoggingFilter filter = new RequestLoggingFilter(currentUser(null));
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/usuarios");
    MockHttpServletResponse response = new MockHttpServletResponse();

    filter.doFilter(request, response, new MockFilterChain());

    assertEquals(200, response.getStatus());
  }

  @Test
  void shouldIgnoreRequestsOutsideApi() throws ServletException, IOException {
    RequestLoggingFilter filter = new RequestLoggingFilter(currentUser("jane.doe"));
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/docs");
    MockHttpServletResponse response = new MockHttpServletResponse();

    filter.doFilter(request, response, new MockFilterChain());

    assertEquals(200, response.getStatus());
  }

  private static CurrentAuthenticatedUser currentUser(String username) {
    return new CurrentAuthenticatedUser() {
      @Override
      public java.util.Optional<AuthenticatedUser> get() {
        if (username == null) {
          return java.util.Optional.empty();
        }

        return java.util.Optional.of(
            new AuthenticatedUser(
                "subject",
                username,
                "jane@example.com",
                "Jane Doe",
                "00000000000",
                "91999999999",
                java.time.LocalDate.of(1990, 1, 1),
                java.util.Set.of(),
                java.util.Map.of(),
                java.util.Set.of()));
      }
    };
  }
}
