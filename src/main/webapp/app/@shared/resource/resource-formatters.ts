const dateTimeFormatter = new Intl.DateTimeFormat('pt-BR', {
  dateStyle: 'short',
  timeStyle: 'short',
});

export function orDash(value?: string | null): string {
  return value?.trim() ? value : '—';
}

export function shortId(value?: string | null): string {
  if (!value?.trim()) {
    return '—';
  }

  return value.length > 8 ? `${value.slice(0, 8)}...` : value;
}

export function formatDateTime(value?: string | null): string {
  if (!value?.trim()) {
    return '—';
  }

  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? value : dateTimeFormatter.format(date);
}

export function formatBytes(bytes: number): string {
  if (!Number.isFinite(bytes) || bytes <= 0) {
    return '0 B';
  }

  if (bytes < 1024) {
    return `${bytes} B`;
  }

  if (bytes < 1024 ** 2) {
    return `${(bytes / 1024).toFixed(1)} KB`;
  }

  return `${(bytes / 1024 ** 2).toFixed(1)} MB`;
}

export function joinValues(values: readonly string[]): string {
  return values.length ? values.map((value) => shortId(value)).join(', ') : '—';
}
