import { useState } from "react";
import {
  createExperience,
  extractExperiences,
  searchExperiences,
  generateCoverLetter,
} from "./api";
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

  const [longText, setLongText] = useState("");
  const [extractedExperiences, setExtractedExperiences] = useState([]);

  const [recommendedExperiences, setRecommendedExperiences] = useState([]);
  const [selectedExperienceId, setSelectedExperienceId] = useState(null);

  const [generatedResult, setGeneratedResult] = useState(null);

  const [experienceLoading, setExperienceLoading] = useState(false);
  const [extractLoading, setExtractLoading] = useState(false);
  const [recommendLoading, setRecommendLoading] = useState(false);
  const [generateLoading, setGenerateLoading] = useState(false);

  const handleExperienceChange = (e) => {
    const { name, value } = e.target;

    setExperienceForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleCoverLetterChange = (e) => {
    const { name, value } = e.target;

    setCoverLetterForm((prev) => ({
      ...prev,
      [name]: value,
    }));

    if (name === "company" || name === "position" || name === "question") {
      setSelectedExperienceId(null);
      setRecommendedExperiences([]);
    }
  };

  const handleExtractExperiences = async (e) => {
    e.preventDefault();

    if (!longText.trim()) {
      alert("추출할 텍스트를 입력해주세요.");
      return;
    }

    try {
      setExtractLoading(true);

      const result = await extractExperiences({
        content: longText,
      });

      setExtractedExperiences(result.extractedExperiences || []);
    } catch (error) {
      console.error(error);

      const status = error.response?.status;
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        error.message;

      alert(`경험 추출에 실패했습니다.\nstatus: ${status}\nmessage: ${message}`);
    } finally {
      setExtractLoading(false);
    }
  };

  const handleSaveExtractedExperience = async (experience) => {
    try {
      await createExperience({
        title: experience.title || "",
        category: experience.category || "",
        period: experience.period || "",
        content: experience.content || "",
        tags: experience.tags || [],
      });

      alert("추출된 경험이 저장되었습니다.");
    } catch (error) {
      console.error(error);

      const status = error.response?.status;
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        error.message;

      alert(`경험 저장에 실패했습니다.\nstatus: ${status}\nmessage: ${message}`);
    }
  };

  const handleCreateExperience = async (e) => {
    e.preventDefault();

    try {
      setExperienceLoading(true);

      await createExperience({
        title: experienceForm.title,
        category: experienceForm.category,
        period: experienceForm.period,
        content: experienceForm.content,
        tags: experienceForm.tags
          .split(",")
          .map((tag) => tag.trim())
          .filter(Boolean),
      });

      alert("경험이 저장되었습니다.");

      setExperienceForm({
        title: "",
        category: "",
        period: "",
        content: "",
        tags: "",
      });
    } catch (error) {
      console.error(error);

      const status = error.response?.status;
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        error.message;

      alert(`경험 저장에 실패했습니다.\nstatus: ${status}\nmessage: ${message}`);
    } finally {
      setExperienceLoading(false);
    }
  };

  const handleSearchRecommendedExperiences = async () => {
    if (!coverLetterForm.question.trim()) {
      alert("먼저 자기소개서 문항을 입력해주세요.");
      return;
    }

    try {
      setRecommendLoading(true);
      setSelectedExperienceId(null);

      const query = [
        coverLetterForm.company,
        coverLetterForm.position,
        coverLetterForm.question,
      ]
        .filter(Boolean)
        .join(" ");

      const result = await searchExperiences(query);

      setRecommendedExperiences(result || []);
    } catch (error) {
      console.error(error);

      const status = error.response?.status;
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        error.message;

      alert(`관련 경험 검색에 실패했습니다.\nstatus: ${status}\nmessage: ${message}`);
    } finally {
      setRecommendLoading(false);
    }
  };

  const handleGenerateCoverLetter = async (e) => {
    e.preventDefault();

    if (!selectedExperienceId) {
      alert("자기소개서에 사용할 경험을 먼저 선택해주세요.");
      return;
    }

    try {
      setGenerateLoading(true);

      const result = await generateCoverLetter({
        company: coverLetterForm.company,
        position: coverLetterForm.position,
        question: coverLetterForm.question,
        maxLength: Number(coverLetterForm.maxLength),
        experienceId: selectedExperienceId,
      });

      setGeneratedResult(result);
    } catch (error) {
      console.error(error);

      const status = error.response?.status;
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        error.message;

      alert(
        `자기소개서 생성에 실패했습니다.\nstatus: ${status}\nmessage: ${message}`
      );
    } finally {
      setGenerateLoading(false);
    }
  };

  const selectedExperience = recommendedExperiences.find(
    (experience) => experience.experienceId === selectedExperienceId
  );

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="logo-box">
          <div className="logo-icon">C</div>
          <div>
            <h1>CoverLetter RAG</h1>
            <p>Experience-based AI writing</p>
          </div>
        </div>

        <nav className="side-nav">
          <a href="#extract">경험 추출</a>
          <a href="#experience">경험 등록</a>
          <a href="#generate">자소서 생성</a>
          <a href="#result">생성 결과</a>
        </nav>

        <div className="side-card">
          <span>RAG Pipeline</span>
          <strong>MariaDB + Qdrant + OpenAI</strong>
          <p>Qdrant로 후보를 찾고, 선택한 경험 하나로 답변을 생성합니다.</p>
        </div>
      </aside>

      <main className="main">
        <section className="hero">
          <div>
            <span className="eyebrow">Personal AX Service</span>
            <h2>내 경험을 기반으로 자기소개서 초안을 생성하세요.</h2>
            <p>
              이력서와 포트폴리오 내용을 경험 카드로 정리하고, 문항에 맞는
              경험을 선택해 일관된 자기소개서 답변을 생성합니다.
            </p>
          </div>

          <div className="hero-stats">
            <div>
              <strong>Top-K</strong>
              <span>관련 경험 추천</span>
            </div>
            <div>
              <strong>1 Pick</strong>
              <span>선택 경험 기반 생성</span>
            </div>
            <div>
              <strong>RAG</strong>
              <span>Qdrant + OpenAI</span>
            </div>
          </div>
        </section>

        <section className="grid">
          <article id="extract" className="panel wide">
            <div className="panel-header">
              <div>
                <span className="section-label">Step 01</span>
                <h3>긴 텍스트에서 경험 추출</h3>
              </div>
              <span className="badge">LLM Extraction</span>
            </div>

            <p className="panel-desc">
              이력서, 자기소개서, 포트폴리오 내용을 붙여넣으면 LLM이
              자기소개서에 활용 가능한 경험 후보를 구조화합니다.
            </p>

            <form onSubmit={handleExtractExperiences} className="form">
              <textarea
                placeholder="예: DOF 엔진팀 인턴으로 근무하며 TOF 카메라 SDK를 활용해..."
                value={longText}
                onChange={(e) => setLongText(e.target.value)}
                rows={9}
              />

              <button
                type="submit"
                className="primary-btn"
                disabled={extractLoading}
              >
                {extractLoading ? "경험 추출 중..." : "경험 추출하기"}
              </button>
            </form>

            {extractedExperiences.length > 0 && (
              <div className="extracted-list">
                <div className="sub-header">
                  <h4>추출된 경험 후보</h4>
                  <span>{extractedExperiences.length}개</span>
                </div>

                {extractedExperiences.map((experience, index) => (
                  <div key={index} className="experience-preview">
                    <div className="preview-top">
                      <div>
                        <h5>{experience.title}</h5>
                        <p>
                          {experience.category || "category 없음"} ·{" "}
                          {experience.period || "기간 없음"}
                        </p>
                      </div>

                      <button
                        type="button"
                        className="ghost-btn"
                        onClick={() =>
                          handleSaveExtractedExperience(experience)
                        }
                      >
                        저장
                      </button>
                    </div>

                    <p className="preview-content">{experience.content}</p>

                    <div className="tag-list">
                      {(experience.tags || []).map((tag, tagIndex) => (
                        <span key={tagIndex} className="tag">
                          {tag}
                        </span>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </article>

          <article id="experience" className="panel">
            <div className="panel-header">
              <div>
                <span className="section-label">Step 02</span>
                <h3>경험 직접 등록</h3>
              </div>
              <span className="badge green">Vectorize</span>
            </div>

            <form onSubmit={handleCreateExperience} className="form">
              <input
                name="title"
                placeholder="경험 제목"
                value={experienceForm.title}
                onChange={handleExperienceChange}
                required
              />

              <div className="two-col">
                <input
                  name="category"
                  placeholder="카테고리"
                  value={experienceForm.category}
                  onChange={handleExperienceChange}
                />

                <input
                  name="period"
                  placeholder="기간"
                  value={experienceForm.period}
                  onChange={handleExperienceChange}
                />
              </div>

              <textarea
                name="content"
                placeholder="경험 내용을 입력하세요."
                value={experienceForm.content}
                onChange={handleExperienceChange}
                rows={7}
                required
              />

              <input
                name="tags"
                placeholder="태그: Spring, 협업, 문제 해결"
                value={experienceForm.tags}
                onChange={handleExperienceChange}
              />

              <button
                type="submit"
                className="primary-btn"
                disabled={experienceLoading}
              >
                {experienceLoading ? "저장 중..." : "경험 저장하기"}
              </button>
            </form>
          </article>

          <article id="generate" className="panel">
            <div className="panel-header">
              <div>
                <span className="section-label">Step 03</span>
                <h3>자기소개서 생성</h3>
              </div>
              <span className="badge purple">Select & Generate</span>
            </div>

            <form onSubmit={handleGenerateCoverLetter} className="form">
              <div className="two-col">
                <input
                  name="company"
                  placeholder="회사명"
                  value={coverLetterForm.company}
                  onChange={handleCoverLetterChange}
                  required
                />

                <input
                  name="position"
                  placeholder="지원 직무"
                  value={coverLetterForm.position}
                  onChange={handleCoverLetterChange}
                  required
                />
              </div>

              <textarea
                name="question"
                placeholder="자기소개서 문항을 입력하세요."
                value={coverLetterForm.question}
                onChange={handleCoverLetterChange}
                rows={7}
                required
              />

              <button
                type="button"
                className="secondary-btn"
                onClick={handleSearchRecommendedExperiences}
                disabled={recommendLoading}
              >
                {recommendLoading ? "관련 경험 찾는 중..." : "관련 경험 찾기"}
              </button>

              {recommendedExperiences.length > 0 && (
                <div className="recommend-list">
                  <div className="sub-header">
                    <h4>관련 경험 후보</h4>
                    <span>{recommendedExperiences.length}개</span>
                  </div>

                  {recommendedExperiences.map((experience) => (
                    <button
                      key={experience.experienceId}
                      type="button"
                      className={
                        selectedExperienceId === experience.experienceId
                          ? "recommend-card selected"
                          : "recommend-card"
                      }
                      onClick={() =>
                        setSelectedExperienceId(experience.experienceId)
                      }
                    >
                      <div className="recommend-card-top">
                        <strong>{experience.title}</strong>
                        <em>
                          score{" "}
                          {experience.score?.toFixed?.(3) ?? experience.score}
                        </em>
                      </div>

                      <p>
                        {experience.category || "category 없음"} ·{" "}
                        {experience.period || "기간 없음"}
                      </p>

                      <span>{experience.content}</span>
                    </button>
                  ))}
                </div>
              )}

              {selectedExperience && (
                <div className="selected-experience-box">
                  <strong>선택한 경험</strong>
                  <p>{selectedExperience.title}</p>
                </div>
              )}

              <input
                name="maxLength"
                type="number"
                placeholder="글자 수 제한"
                value={coverLetterForm.maxLength}
                onChange={handleCoverLetterChange}
              />

              <button
                type="submit"
                className="primary-btn"
                disabled={generateLoading || !selectedExperienceId}
              >
                {generateLoading ? "생성 중..." : "선택한 경험으로 생성하기"}
              </button>
            </form>
          </article>
        </section>

        {generatedResult && (
          <section id="result" className="result-panel">
            <div className="result-header">
              <div>
                <span className="section-label">Generated Draft</span>
                <h3>
                  {generatedResult.company} · {generatedResult.position}
                </h3>
              </div>

              <span className="badge">Saved #{generatedResult.id}</span>
            </div>

            <div className="question-box">
              <strong>문항</strong>
              <p>{generatedResult.question}</p>
            </div>

            <div className="answer-box">
              <strong>생성된 답변 초안</strong>
              <p>{generatedResult.answer}</p>
            </div>

            {generatedResult.usedExperiences?.length > 0 && (
              <div className="used-box">
                <strong>사용된 경험</strong>

                <div className="used-list">
                  {generatedResult.usedExperiences.map((exp) => (
                    <div key={exp.experienceId} className="used-item">
                      <span>{exp.title}</span>
                      <em>score {exp.score?.toFixed?.(3) ?? exp.score}</em>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </section>
        )}
      </main>
    </div>
  );
}

export default App;