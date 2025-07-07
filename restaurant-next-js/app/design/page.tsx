import "../globals.css";
import Header from "@/components/Header";

export default function DesignPage() {
  return (
    <>
      <section className="m-4 p-4 bg-neutral-100 rounded-lg">
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
      <section className="m-4 p-4 bg-neutral-100 rounded-lg">
        <h1 className="heading-1 mb-4">Typography</h1>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 max-w-[600px]">
          <div>
            <h1 className="heading-1">Heading 1</h1>
            <p className="text-xs text-neutral-500">.heading-1</p>
          </div>
          <div>
            <h2 className="heading-2">Heading 2</h2>
            <p className="text-xs text-neutral-500">.heading-2</p>
          </div>
          <div>
            <h3 className="heading-3">Heading 3</h3>
            <p className="text-xs text-neutral-500">.heading-3</p>
          </div>
          <div>
            <p className="block-title-text">Block title</p>
            <p className="text-xs text-neutral-500">.block-title-text</p>
          </div>
          <div>
            <p className="body-text">Body</p>
            <p className="text-xs text-neutral-500">.body-text</p>
          </div>
          <div>
            <p className="body-bold-text">Body Bold</p>
            <p className="text-xs text-neutral-500">.body-bold-text</p>
          </div>
          <div>
            <p className="btn-text">Button</p>
            <p className="text-xs text-neutral-500">.btn-text</p>
          </div>
          <div>
            <p className="caption-text">Caption</p>
            <p className="text-xs text-neutral-500">.caption-text</p>
          </div>
          <div>
            <p className="link-text">Link</p>
            <p className="text-xs text-neutral-500">.link-text</p>
          </div>
          <div>
            <p className="nav-text">Navigation</p>
            <p className="text-xs text-neutral-500">.nav-text</p>
          </div>
        </div>
      </section>

      <section className="m-4 p-4 bg-neutral-100 rounded-lg flex flex-col gap-2">
        <h1 className="heading-1 mb-4">Header</h1>
        <Header isAuthenticated={false} role="" />
        <p className="text-xs text-neutral-500">Not logged in</p>
        <Header isAuthenticated={true} role="user" />
        <p className="text-xs text-neutral-500">Logged in</p>
        <Header isAuthenticated={true} role="waiter" />
        <p className="text-xs text-neutral-500">Waiter role</p>
      </section>
    </>
  );
}