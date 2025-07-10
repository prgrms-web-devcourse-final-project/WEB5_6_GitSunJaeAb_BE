export class QuestDTO {

  constructor(data:Partial<QuestDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  questImage?: string|null;
  description?: string|null;
  isActive?: boolean|null;
  createdAt?: string|null;
  completedAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  member?: number|null;

}
