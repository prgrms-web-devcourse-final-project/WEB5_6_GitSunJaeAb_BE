export class RoadmapHashtagRelationDTO {

  constructor(data:Partial<RoadmapHashtagRelationDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  hashtag?: number|null;
  roadmap?: number|null;

}
