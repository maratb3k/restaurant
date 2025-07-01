import "../globals.css";

export default function DesignPage() {
  return (
    <>
      <section className="m-4 mb-[100px]">
        <h1 className="heading-1 mb-4">Buttons</h1>

        <div className="grid grid-cols-2 gap-4 w-[600px]">
          <button className="btn btn-lg">Button large</button>
          <button className="btn btn-lg" disabled>Button large disabled</button>
          <button className="btn">Button</button>
          <button className="btn" disabled>Button disabled</button>
          <button className="btn btn-sm">Button small</button>
          <button className="btn btn-sm" disabled>Button small disabled</button>
          <button className="btn btn-secondary">Button secondary</button>
          <button className="btn btn-secondary" disabled>Button secondary disabled</button>
          <button className="btn btn-sm btn-secondary">Button secondary small</button>
          <button className="btn btn-sm btn-secondary" disabled>Button secondary small disabled</button>

        </div>
      </section>
      <section className="m-4">
        <h1 className="heading-1 mb-4">Typography</h1>

        <div className="grid grid-cols-2 gap-4 w-[600px]">
          <h1 className="heading-1">Heading 1</h1>
          <h2 className="heading-2">Heading 2</h2>
          <h3 className="heading-3">Heading 3</h3>
          <p className="block-title-text">Block title</p>
          <p className="body-text">Body</p>
          <p className="body-bold-text">Body Bold</p>
          <p className="btn-text">Button</p>
          <p className="caption-text">Caption</p>
          <p className="link-text">Link</p>
          <p className="nav-text">Navigation</p>
        </div>
      </section>
    </>
  );
}