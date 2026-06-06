import type { Router } from '@angular/router';
import { type Route, type UrlSegment, type UrlTree } from '@angular/router';

export function buildMatchUrl(route: Route, segments: UrlSegment[]): string {
  const matchedPath = segments.map((segment) => segment.path).join('/');
  return `/${matchedPath || route.path || ''}`.replace(/\/{2,}/g, '/');
}

export function buildRedirectTree(router: Router, redirectTo: string): UrlTree {
  return router.createUrlTree([redirectTo]);
}
