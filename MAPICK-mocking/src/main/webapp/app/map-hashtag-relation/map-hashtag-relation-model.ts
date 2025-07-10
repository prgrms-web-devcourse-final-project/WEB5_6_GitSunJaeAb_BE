export class MapHashtagRelationDTO {

  constructor(data:Partial<MapHashtagRelationDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  hashtag?: number|null;
  map?: number|null;

}
