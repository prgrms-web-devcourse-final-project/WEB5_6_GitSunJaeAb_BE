export class RoadMapCategoryRelationDTO {

  constructor(data:Partial<RoadMapCategoryRelationDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  roadmap?: number|null;

}
