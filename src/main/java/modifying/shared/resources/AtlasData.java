package modifying.shared.resources;

import java.util.List;

// 唯一公开的入口类
public record AtlasData(List<FrameData> frames, MetaData meta) {}

// 以下 record 不加 public（包私有），只在本包内可用
record FrameData(String filename, FrameRect frame, boolean rotated,
                 boolean trimmed, SpriteSourceSize spriteSourceSize,
                 SourceSize sourceSize) {}

record FrameRect(int x, int y, int w, int h) {}

record SpriteSourceSize(int x, int y, int w, int h) {}

record SourceSize(int w, int h) {}

record MetaData(String app, String version, String image,
                String format, Size size, String scale, String smartupdate) {}

record Size(int w, int h) {}