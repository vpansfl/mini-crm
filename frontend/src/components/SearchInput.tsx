type SearchInputProps = {
  value: string
  onChange: (value: string) => void
  placeholder?: string
}

// pill 모양 검색 입력 (docs/design-system/components.md "폼")
function SearchInput({ value, onChange, placeholder }: SearchInputProps) {
  return (
    <label className="search-field">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" aria-hidden="true">
        <circle cx="11" cy="11" r="7" />
        <path d="m20 20-3.5-3.5" strokeLinecap="round" />
      </svg>
      <input
        className="search-input"
        type="search"
        value={value}
        placeholder={placeholder}
        aria-label={placeholder ?? '검색'}
        onChange={(event) => onChange(event.target.value)}
      />
    </label>
  )
}

export default SearchInput
