export class RoadmapDTO {

  constructor(data:Partial<RoadmapDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  description?: string|null;
  thumbnail?: string|null;
  isPublic?: boolean|null;
  isAnimated?: boolean|null;
  likeCount?: number|null;
  viewCount?: number|null;
  roadmapType?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  member?: number|null;
  originalRoadmap?: number|null;

}
