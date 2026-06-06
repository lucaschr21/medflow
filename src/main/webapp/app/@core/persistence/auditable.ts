/**
 * Contrato base equivalente ao `Auditable` do backend.
 *
 * A interface representa os campos comuns expostos por recursos persistidos da
 * aplicação, incluindo identificador, versão otimista e metadados de auditoria.
 */
export interface Auditable {
  readonly id: string;
  readonly version: number;
  readonly createdBy: string;
  readonly createdAt: string;
  readonly lastModifiedBy: string;
  readonly lastModifiedAt: string;
}
