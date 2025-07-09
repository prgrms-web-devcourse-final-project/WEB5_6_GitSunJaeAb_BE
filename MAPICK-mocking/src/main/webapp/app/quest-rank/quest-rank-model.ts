export class QuestRankDTO {

  constructor(data:Partial<QuestRankDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  rank?: number|null;
  completedAt?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  quest?: number|null;
  member?: number|null;

}
