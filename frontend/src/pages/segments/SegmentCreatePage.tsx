import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router'
import { useCreateSegment, useSegmentPreview } from '../../api/segments.ts'
import PageHeader from '../../components/PageHeader.tsx'
import {
  SEGMENT_FIELDS,
  SEGMENT_OPERATOR_LABELS,
  operatorRequiresValue,
  operatorsFor,
  segmentValueType,
} from '../../constants/labels.ts'
import { ROUTES, segmentDetailPath } from '../../constants/routes.ts'
import { useDebouncedValue } from '../../hooks/useDebouncedValue.ts'
import type { SegmentField, SegmentOperator, SegmentRule } from '../../types/segment.ts'
import { formatNumber } from '../../utils/format.ts'

function SegmentCreatePage() {
  const navigate = useNavigate()
  const createSegment = useCreateSegment()

  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [ruleField, setRuleField] = useState<SegmentField>('COUNTRY')
  const [ruleOperator, setRuleOperator] = useState<SegmentOperator>('EQ')
  const [ruleValue, setRuleValue] = useState('')

  const valueType = segmentValueType(ruleField)
  const operators = operatorsFor(ruleField)
  const needsValue = operatorRequiresValue(ruleOperator)

  // 입력이 완성된 조건만 미리보기한다. (값이 필요한 연산자인데 값이 비어 있으면 null)
  const rule = useMemo<SegmentRule | null>(() => {
    const value = ruleValue.trim()
    if (needsValue && !value) {
      return null
    }
    return { ruleField, ruleOperator, ruleValue: needsValue ? value : null }
  }, [ruleField, ruleOperator, ruleValue, needsValue])
  const debouncedRule = useDebouncedValue(rule)
  const preview = useSegmentPreview(debouncedRule)

  const handleFieldChange = (field: SegmentField) => {
    setRuleField(field)
    const nextOperators = operatorsFor(field)
    if (!nextOperators.includes(ruleOperator)) {
      setRuleOperator(nextOperators[0])
    }
    if (segmentValueType(field) !== valueType) {
      setRuleValue('')
    }
  }

  const handleSubmit = (event: { preventDefault: () => void }) => {
    event.preventDefault()
    if (!rule || createSegment.isPending) {
      return
    }
    createSegment.mutate(
      { name: name.trim(), description: description.trim() || null, ...rule },
      { onSuccess: (segment) => navigate(segmentDetailPath(segment.id)) },
    )
  }

  const canSubmit = name.trim() !== '' && rule !== null && !createSegment.isPending

  return (
    <>
      <PageHeader
        title="새 세그먼트"
        description="조건 하나로 고객 그룹을 만듭니다. 저장하면 현재 고객 데이터로 멤버가 계산됩니다."
        back={{ to: ROUTES.SEGMENTS, label: '세그먼트 목록' }}
      />

      {createSegment.isError && <div className="notice notice-error">저장 실패: {createSegment.error.message}</div>}

      <form className="form card" onSubmit={handleSubmit}>
        <label className="field">
          <span>이름</span>
          <input
            className="input"
            value={name}
            maxLength={100}
            required
            placeholder="예: 한국 고객"
            onChange={(event) => setName(event.target.value)}
          />
        </label>

        <label className="field">
          <span>설명 (선택)</span>
          <input
            className="input"
            value={description}
            maxLength={500}
            onChange={(event) => setDescription(event.target.value)}
          />
        </label>

        <div className="form-row">
          <label className="field">
            <span>조건 필드</span>
            <select
              className="select"
              value={ruleField}
              onChange={(event) => handleFieldChange(event.target.value as SegmentField)}
            >
              {SEGMENT_FIELDS.map((field) => (
                <option key={field.value} value={field.value}>
                  {field.label}
                </option>
              ))}
            </select>
          </label>

          <label className="field">
            <span>연산자</span>
            <select
              className="select"
              value={ruleOperator}
              onChange={(event) => setRuleOperator(event.target.value as SegmentOperator)}
            >
              {operators.map((operator) => (
                <option key={operator} value={operator}>
                  {SEGMENT_OPERATOR_LABELS[operator]}
                </option>
              ))}
            </select>
          </label>

          <label className="field">
            <span>값</span>
            <input
              className="input"
              type={valueType === 'DATE' ? 'date' : 'text'}
              value={needsValue ? ruleValue : ''}
              disabled={!needsValue}
              maxLength={255}
              placeholder={needsValue ? (valueType === 'DATE' ? '' : '예: KR') : '값 불필요'}
              onChange={(event) => setRuleValue(event.target.value)}
            />
          </label>
        </div>

        <div className="notice">
          {rule === null
            ? '조건 값을 입력하면 대상 고객 수를 미리 보여줍니다.'
            : preview.isError
              ? `미리보기 실패: ${preview.error.message}`
              : preview.isSuccess && debouncedRule === rule
                ? `조건에 맞는 고객: ${formatNumber(preview.data.customerCount)}명`
                : '대상 고객 수 계산 중...'}
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={!canSubmit}>
            {createSegment.isPending ? '저장 중...' : '세그먼트 생성'}
          </button>
          <button type="button" className="btn btn-ghost" onClick={() => navigate(ROUTES.SEGMENTS)}>
            취소
          </button>
        </div>
      </form>
    </>
  )
}

export default SegmentCreatePage
