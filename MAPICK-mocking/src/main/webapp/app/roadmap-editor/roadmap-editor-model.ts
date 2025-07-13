export class RoadmapEditorDTO {

  constructor(data:Partial<RoadmapEditorDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  permission?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  roadmap?: number|null;
  member?: number|null;
  invitedBy?: number|null;

}
