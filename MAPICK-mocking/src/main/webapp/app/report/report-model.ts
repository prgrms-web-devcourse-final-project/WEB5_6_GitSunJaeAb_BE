export class ReportDTO {

  constructor(data:Partial<ReportDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  reportType?: string|null;
  description?: string|null;
  status?: string|null;
  createdAt?: string|null;
  resolvedAt?: string|null;
  reporter?: number|null;
  reportedMember?: number|null;
  roadmap?: number|null;
  marker?: number|null;
  quest?: number|null;

}
