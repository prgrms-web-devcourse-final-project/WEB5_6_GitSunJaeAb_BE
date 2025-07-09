export class MapEditorDTO {

  constructor(data:Partial<MapEditorDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  permission?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  map?: number|null;
  member?: number|null;
  invitedBy?: number|null;

}
