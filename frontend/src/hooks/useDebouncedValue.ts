import { useEffect, useState } from 'react'

// 입력이 멈춘 뒤 delay(ms)가 지나면 값을 반영한다. (입력 중 API 호출 줄이기)
export function useDebouncedValue<T>(value: T, delay = 300): T {
  const [debounced, setDebounced] = useState(value)

  useEffect(() => {
    const timer = setTimeout(() => setDebounced(value), delay)
    return () => clearTimeout(timer)
  }, [value, delay])

  return debounced
}
