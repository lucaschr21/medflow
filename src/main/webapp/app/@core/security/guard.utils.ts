import type { Router } from '@angular/router';
import { type UrlTree } from '@angular/router';

export function buildRedirectTree(router: Router, redirectTo: string): UrlTree {
  return router.parseUrl(redirectTo);
}
