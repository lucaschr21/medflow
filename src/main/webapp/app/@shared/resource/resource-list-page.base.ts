import { computed } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';

import type { FindAllParams } from '../../@core/persistence/find-all.params';
import type { PageResult } from '../../@core/persistence/page-result';
import { ProtectedResourceService } from '../../@core/security/authorization/protected-resource.service';
import type { Resource, Scope } from '../../@core/security/authorization/authorization.types';

const EMPTY_PAGE: PageResult<never> = {
  content: [],
  page: 0,
  size: 0,
  totalElements: 0,
  totalPages: 0,
};

export abstract class ResourceListPageBase<Entity> {
  protected abstract readonly service: ProtectedResourceService<
    Resource,
    Extract<Scope, 'delete' | 'deactivate'>,
    Entity,
    unknown
  >;

  protected buildFindAllParams(): FindAllParams {
    return {
      page: 0,
      size: 10,
    };
  }

  protected readonly pageResource = rxResource({
    defaultValue: EMPTY_PAGE as PageResult<Entity>,
    stream: () => this.service.findAll(this.buildFindAllParams()),
  });

  readonly loading = this.pageResource.isLoading;
  readonly entities = computed(() => this.pageResource.value().content);
  readonly total = computed(() => this.pageResource.value().totalElements);
}
