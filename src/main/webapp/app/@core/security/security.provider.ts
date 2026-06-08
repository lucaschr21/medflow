import { makeEnvironmentProviders, type EnvironmentProviders } from '@angular/core';

export type SecurityFeature = () => EnvironmentProviders;

export function provideSecurity(...features: readonly SecurityFeature[]): EnvironmentProviders {
  return makeEnvironmentProviders([...features.map((feature) => feature())]);
}
