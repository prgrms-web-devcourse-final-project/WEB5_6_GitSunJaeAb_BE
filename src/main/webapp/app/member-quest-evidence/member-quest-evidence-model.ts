export class MemberQuestEvidenceDTO {

  constructor(data:Partial<MemberQuestEvidenceDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  imageUrl?: string|null;
  description?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  memberQuest?: number|null;

}
