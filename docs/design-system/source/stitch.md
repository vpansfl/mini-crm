<!-- Stitch에서 제공한 원본 디자인 시스템. 수정하지 않는다. CRM 적용 규칙은 ../README.md 이하 문서를 따른다. -->

---
name: Warm Honey Delivery
colors:
  surface: '#f9f9ff'
  surface-dim: '#d3daea'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f0f3ff'
  surface-container: '#e7eefe'
  surface-container-high: '#e2e8f8'
  surface-container-highest: '#dce2f3'
  on-surface: '#151c27'
  on-surface-variant: '#534434'
  inverse-surface: '#2a313d'
  inverse-on-surface: '#ebf1ff'
  outline: '#867461'
  outline-variant: '#d8c3ad'
  surface-tint: '#855300'
  primary: '#855300'
  on-primary: '#ffffff'
  primary-container: '#f59e0b'
  on-primary-container: '#613b00'
  inverse-primary: '#ffb95f'
  secondary: '#555f6f'
  on-secondary: '#ffffff'
  secondary-container: '#d6e0f3'
  on-secondary-container: '#596373'
  tertiary: '#006c49'
  on-tertiary: '#ffffff'
  tertiary-container: '#30c88f'
  on-tertiary-container: '#004e34'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffddb8'
  primary-fixed-dim: '#ffb95f'
  on-primary-fixed: '#2a1700'
  on-primary-fixed-variant: '#653e00'
  secondary-fixed: '#d9e3f6'
  secondary-fixed-dim: '#bdc7d9'
  on-secondary-fixed: '#121c2a'
  on-secondary-fixed-variant: '#3d4756'
  tertiary-fixed: '#6ffbbe'
  tertiary-fixed-dim: '#4edea3'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005236'
  background: '#f9f9ff'
  on-background: '#151c27'
  surface-variant: '#dce2f3'
typography:
  headline-xl:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-xl-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 26px
    fontWeight: '800'
    lineHeight: 34px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
    letterSpacing: -0.015em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '700'
    lineHeight: 26px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '700'
    lineHeight: 18px
    letterSpacing: -0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.25rem
  gutter-desktop: 1.5rem
  margin: 1rem
  margin-tablet: 2rem
  margin-desktop: 3rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system channels the warmth, rapid delight, and playful efficiency characteristic of contemporary Korean hyper-local food delivery platforms. It blends utilitarian delivery logistics with an inviting street-food appetite appeal. The design style combines **Modern Tactile Minimalist** structure with **Playful Utility**: clean white canvas foundations, deep charcoal clarity, and vibrant sunny yellow accents that invoke hunger, speed, and optimism.

The visual tone is friendly yet punchy, ditching corporate rigidity in favor of rounded container geometries, bouncy interactive feedback, and high-visibility status cues (delivery times, discount badges, and rider tracking metrics).

## Colors

The palette relies on high-contrast accessibility anchored by cheerful, warm food tones:

- **Primary (`#F59E0B`)**: Radiant golden honey. Used for high-impact brand statements, active state pills, hero action buttons, and active tabs. Secondary tones include `#FBBF24` for soft badge fills and `#D97706` for pressed interactive states and text on yellow.
- **Secondary / Primary Text (`#1F2937`)**: Deep charcoal black. Replaces pure black to retain warmth while maintaining legibility for Korean and Latin typography.
- **Surface & Canvas**: Background sits on `#FBFBFA` (warm porcelain) transitioning to `#FFFFFF` for elevated restaurant cards, floating checkout sheets, and modal sheets. Neutral strokes rest at `#E5E7EB`.
- **Muted Gray (`#6B7280`)**: Secondary descriptions, store metadata, distance markers, and inactive icon states.
- **Accents & System Signals**: 
  - Free delivery tag: Refreshing mint green (`#10B981`) paired with soft emerald tint background (`#ECFDF5`).
  - Hot discounts & spicy badges: Punchy red (`#EF4444`) with soft tint background (`#FEF2F2`).
  - Star ratings: Amber gold (`#F59E0B`).

## Typography

The type scale mirrors the structural density and legibility required for Korean platform interfaces, pairing gracefully with modern CJK web fonts (such as Pretendard and Noto Sans KR fallback stacks). 

- **Display & Store Titles**: Strong 700 to 800 weights give character names and restaurant titles crisp clarity even at high density.
- **Numbers & Metrics**: Numeric scores (ratings, delivery times, price tags) are rendered in bold weight with tight letter-spacing to support quick visual scanning across lists.
- **Korean Script Pairing**: When integrating Korean font assets, apply regular letter-spacing (`-0.02em`) and ample line height to maintain optical breathing room for multi-syllable food descriptions.

## Layout & Spacing

Built strictly on an 8-point base grid (with a 4-point micro step), the layout balances dense catalog browsing with breathable mobile navigation:

- **Mobile Viewport (< 640px)**: 1-column fluid feed with full-width horizontal carousel strips for food category discovery. Side margins are locked to 16px (`1rem`).
- **Tablet (640px – 1024px)**: 2 to 3-column store feed grid with a 24px (`1.5rem`) margin.
- **Desktop (> 1024px)**: Fixed-width content container centered at max 1200px, utilizing a 4-column store feed grid or split catalog view with real-time sidebar cart summaries.
- **Vertical Rhythm**: Category roundels and badge filters utilize compact `space-xs` and `space-sm` gaps, while restaurant list items employ `space-md` gaps and `space-xl` dividers.

## Elevation & Depth

Visual hierarchy uses warm, sun-diffused ambient drop shadows paired with crisp 1px neutral hairline borders:

- **Flat / Canvas (Level 0)**: `#FBFBFA` canvas with borderless layouts.
- **Cards & Surface (Level 1)**: Pure `#FFFFFF` with a 1px border (`#E5E7EB`) and ambient shadow: `0 2px 8px -2px rgba(31, 41, 55, 0.05)`.
- **Hover & Sticky Headers (Level 2)**: Elevated store card hover or sticky filtering bars: `0 8px 20px -4px rgba(31, 41, 55, 0.08)`.
- **Floating CTA Bar & Overlays (Level 3)**: Floating order cart buttons and bottom sheets: `0 12px 32px -6px rgba(245, 158, 11, 0.28), 0 4px 12px -2px rgba(31, 41, 55, 0.08)`. The primary action shadow carries a subtle golden tint to evoke physical lift and energy.

## Shapes

The interface embraces generous, welcoming curves:
- **Cards & Modal Sheets**: Styled with `rounded-2xl` (16px) corners to evoke modern app-container softness.
- **Badges, Filter Chips & CTA Pills**: Full pill geometry (`rounded-full` / 9999px) for tags like delivery estimate badges, discount stickers, and quantity selectors.
- **Input Fields & Form Elements**: `rounded-xl` (12px) balance structure with smooth friendliness.

## Components

### Buttons
- **Primary Cart CTA**: Deep golden yellow (`#F59E0B`) fill, charcoal (`#1F2937`) label in bold weight, pill-shaped (`rounded-full`), with an active state in `#D97706`.
- **Secondary / Action**: Light warm tint (`#FEF3C7`) background with dark yellow/charcoal text.
- **Floating Cart Bar**: Docked to viewport bottom with 16px inset margins, rounded-2xl geometry, displaying order count pill, store summary, and price total in an all-in-one trigger.

### Filter & Category Chips
- **Category Roundels**: Circular 64px or 72px icon containers with pastel warm fills, paired with bottom-centered 12px labels.
- **Pill Filters**: 36px height, pill-shaped with 1px border (`#E5E7EB`), turning solid `#1F2937` with pure white text when active.

### Store Feed Cards
- Top aspect-ratio (16:9) image with `rounded-2xl` perimeter.
- Overlaid floating pill badges: Top-left discount badge (`#EF4444`), top-right delivery speed badge (`#1F2937` semi-translucent fill with white text).
- Body section: Title (`headline-md`), star rating with amber star icon, review count in muted gray, delivery fee with free delivery badge (`#10B981` tag).

### Form Inputs & Quantity Selectors
- Search bar: Pill-shaped with subtle light gray fill (`#F3F4F6`), leading search icon, clear text button, and quick-filter category chips below.
- Stepper component: Rounded pill container with charcoal increment/decrement icons and centered bold counter.

### Selection Controls
- Checkbox & Radio: Rounded-md (for checkbox) and circular (for radio) with primary yellow fill and charcoal check/dot indicators when active.
