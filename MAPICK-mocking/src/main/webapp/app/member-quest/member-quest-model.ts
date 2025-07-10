export class MemberQuestDTO {

  constructor(data:Partial<MemberQuestDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  status?: string|null;
  answer?: string|null;
  isRecognized?: string|null;
  createdAt?: string|null;
  completedAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  member?: number|null;
  quest?: number|null;

}
