export class MapCategoryRelationDTO {

  constructor(data:Partial<MapCategoryRelationDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  map?: number|null;

}
