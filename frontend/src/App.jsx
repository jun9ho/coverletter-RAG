import { useState } from "react";
import { createExperience, generateCoverLetter } from "./api";
import "./App.css";

function App() {
  const [experienceForm, setExperienceForm] = useState({
    title: "",
    category: "",
    period: "",
    content: "",
    tags: "",
  });

  const [coverLetterForm, setCoverLetterForm] = useState({
    company: "",
    position: "",
    question: "",
    maxLength: 700,
  });

  const [generatedResult, setGeneratedResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleExperienceChange = (e) => {
    const { name, value } = e.target;
    setExperienceForm({
      ...experienceForm,
      [name]: value,
    });
  };

  const handleCoverLetterChange = (e) => {
    const { name, value } = e.target;
    setCoverLetterForm({
      ...coverLetterForm,
      [name]: value,
    });
  };

  const handleCreateExperience = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      const request = {
        title: experienceForm.title,
        category: experienceForm.category,
        period: experienceForm.period,
        content: experienceForm.content,
        tags: experienceForm.tags
          .split(",")
          .map((tag) => tag.trim())
          .filter((tag) => tag.length > 0),
      };

      await createExperience(request);

      alert("경험이 등록되었습니다.");

      setExperienceForm({
        title: "",
        category: "",
        period: "",
        content: "",
        tags: "",
      });
    } catch (error) {
      console.error(error);
      alert("경험 등록에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateCoverLetter = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      const request = {
        company: coverLetterForm.company,
        position: coverLetterForm.position,
        question: coverLetterForm.question,
        maxLength: Number(coverLetterForm.maxLength),
      };

      const result = await generateCoverLetter(request);
      setGeneratedResult(result);
    } catch (error) {
      console.error(error);
      alert("자기소개서 생성에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <h1>CoverLetter RAG</h1>
        <p>내 경험 기반 자기소개서 생성 서비스</p>
      </header>

      <main className="container">
        <section className="card">
          <h2>1. 경험 등록</h2>

          <form onSubmit={handleCreateExperience} className="form">
            <input
              name="title"
              placeholder="경험 제목 예: DOF 엔진팀 인턴"
              value={experienceForm.title}
              onChange={handleExperienceChange}
              required
            />

            <input
              name="category"
              placeholder="카테고리 예: internship, project, research"
              value={experienceForm.category}
              onChange={handleExperienceChange}
            />

            <input
              name="period"
              placeholder="기간 예: 2023.12~2024.02"
              value={experienceForm.period}
              onChange={handleExperienceChange}
            />

            <textarea
              name="content"
              placeholder="경험 내용을 자세히 입력하세요."
              value={experienceForm.content}
              onChange={handleExperienceChange}
              rows={8}
              required
            />

            <input
              name="tags"
              placeholder="태그 예: 협업, C++, 포인트클라우드"
              value={experienceForm.tags}
              onChange={handleExperienceChange}
            />

            <button type="submit" disabled={loading}>
              {loading ? "처리 중..." : "경험 등록"}
            </button>
          </form>
        </section>

        <section className="card">
          <h2>2. 자기소개서 생성</h2>

          <form onSubmit={handleGenerateCoverLetter} className="form">
            <input
              name="company"
              placeholder="회사명 예: LG CNS"
              value={coverLetterForm.company}
              onChange={handleCoverLetterChange}
              required
            />

            <input
              name="position"
              placeholder="직무 예: AX/DX"
              value={coverLetterForm.position}
              onChange={handleCoverLetterChange}
              required
            />

            <textarea
              name="question"
              placeholder="자기소개서 문항을 입력하세요."
              value={coverLetterForm.question}
              onChange={handleCoverLetterChange}
              rows={5}
              required
            />

            <input
              name="maxLength"
              type="number"
              placeholder="글자 수 제한"
              value={coverLetterForm.maxLength}
              onChange={handleCoverLetterChange}
            />

            <button type="submit" disabled={loading}>
              {loading ? "생성 중..." : "자기소개서 생성"}
            </button>
          </form>
        </section>

        {generatedResult && (
          <section className="card result-card">
            <h2>3. 생성 결과</h2>

            <div className="meta">
              <p>
                <strong>회사:</strong> {generatedResult.company}
              </p>
              <p>
                <strong>직무:</strong> {generatedResult.position}
              </p>
              <p>
                <strong>문항:</strong> {generatedResult.question}
              </p>
            </div>

            <div className="answer">
              <h3>답변 초안</h3>
              <p>{generatedResult.answer}</p>
            </div>

            <div className="used-experiences">
              <h3>사용된 경험</h3>

              {generatedResult.usedExperiences?.map((exp) => (
                <div key={exp.experienceId} className="experience-chip">
                  <strong>{exp.title}</strong>
                  <span>{exp.category}</span>
                  <span>score: {exp.score?.toFixed(4)}</span>
                </div>
              ))}
            </div>
          </section>
        )}
      </main>
    </div>
  );
}

export default App;